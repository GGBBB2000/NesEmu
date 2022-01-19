package com.example.nesemu.nes

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.nesemu.nes.cartridge.Cartridge
import com.example.nesemu.nes.util.Address
import com.example.nesemu.nes.util.IODevice

class Ppu(private val cartridge: Cartridge, val nmi: NMI) : IODevice {
    private val ctrlRegister1 = CtrlRegister1()
    private val ctrlRegister2 = CtrlRegister2()
    private val status = PpuStatus()
    private val spriteMemAddress = MemoryAddress.SpriteMemAddress()
    private val ppuMemAddress = MemoryAddress.PpuMemAddress(ctrlRegister1)
    private val nameTables = Array(4){Array<Byte>(0x3C0){0}}
    private val attributeTables = Array(4){Array<Byte>(0x40){0}}
    private val bgPaletteTables = Array(4){Array<Byte>(4){0}}
    private val chrPaletteTables = Array(4){Array<Byte>(4){0}}
    private var scrollIndex = 0 // xとyのスクロールオフセットの選択
    private val scrollOffset = Array(2){Array(240){0} }

    private val objectAttributeMemory = Array(64){Sprite()}

    val screen = Screen(Array(256 * 240) {0xFF000000.toInt()}, MutableLiveData(false))
    // LiveDataは何書いてもObserverの処理が走るらしい...
    @Suppress("ArrayInDataClass")
    data class Screen(val data: Array<Int>, var isReady: MutableLiveData<Boolean>)

    private var readSyncBuffer: Byte = 0xFF.toByte() // CPUとPPU間の，データread時のクロック同期をとるためのバッファ

    private class Sprite (var y: Int = -1, var index: Int = 0, var attribute: Byte = 0, var x: Int = 0) {
        fun setData(dataIndex: Int, data: Byte) {
            when (dataIndex) {
                0 -> y = (data.toInt() and 0xFF) - 1
                1 -> index = data.toInt() and 0xFF
                2 -> attribute = data
                3 -> x = data.toInt() and 0xFF
            }
        }

        fun getColorPaletteIndex() : Int = attribute.toInt() and 0b11
    }

    sealed class MemoryAddress {
        open var value: Int = 0

        open fun increment() {
            value++
        }

        class SpriteMemAddress : MemoryAddress()

        class PpuMemAddress(private val register1: CtrlRegister1) : MemoryAddress() {
            private var addressIndex = 0
            private var addressArray = arrayOf(0, 0)

            override var value: Int = 0
                get() = super.value
                set(value) {
                    addressArray[addressIndex++ % 2] = value
                    super.value = ((addressArray[0] shl 8) and 0xFF00) or (addressArray[1] and 0xFF)
                    field = value
                }

            override fun increment() {
                super.value += register1.getAdderIncAmount()
            }
        }
    }

    data class CtrlRegister1(var data: Byte = 0) {
        fun isNMIEnable() : Boolean = (data.toInt() and  0b1000_0000) != 0

        /**
         * スプライトの縦の長さを返す 8 or 16
         */
        fun getSpriteSize() : Int = (data.toInt() and 0b0010_0000 ushr 5) * 8 + 8

        fun getBGPatternTableAddress() : Address {
            return if ((data.toInt() and 0b0001_0000 ushr 4) == 0) {
                Address.buildAddress(0x0000)
            } else {
                Address.buildAddress(0x1000)
            }
        }

        fun getSpritePatternTableAddress() : Address {
            return if ((data.toInt() and 0b0000_1000 ushr 3) == 0) {
                Address.buildAddress(0x0000)
            } else {
                Address.buildAddress(0x1000)
            }
        }

        fun getAdderIncAmount() : Int {
           return if (data.toInt() and 0b100 == 0) {
               1
           } else {
               32
           }
        }

        fun getNameTableSelect() : Int = data.toInt() and 0b11
    }

    data class CtrlRegister2(var data: Byte = 0) {
        fun getBGColor() : Int {
            val index = (data.toInt() and 0b1110_000) ushr 4
            return when (index) {
                0b000 -> 0xFF000000.toInt()
                0b001 -> 0xFF00FF00.toInt()
                0b010 -> 0xFF0000FF.toInt()
                0b100 -> 0xFFFF00FF.toInt()
                else -> 0xFF000000.toInt()
            }
        }

        fun spriteEnable() = (data.toInt() and 0b0001_0000) != 0

        fun bgEnable() = (data.toInt() and 0b0000_1000) != 0

        fun spriteMaskEnable() = (data.toInt() and 0b0000_0100) != 0

        fun bgMaskEnable() = (data.toInt() and 0b0000_0010) != 0

        fun colorEnable() = (data.toInt() and 0b0000_0001) == 0
    }

    // TODO PPUの読み込み時は0x2005のアドレス書き込み順序がリセットされる
    private class PpuStatus {
        private var data: Byte = 0

        fun read() : Byte {
            val ret = data
            setVBlank(false)
            return ret
        }

        fun setVBlank(flag: Boolean) {
            data = if (flag) {
                ((data.toInt() and 0xFF) or (0b1000_0000)).toByte()
            } else {
                ((data.toInt() and 0b0110_0000)).toByte()
            }
        }
    }

    val colors: Array<Int> = arrayOf(
        0xFF808080.toInt(), 0xFF003DA6.toInt(), 0xFF0012B0.toInt(), 0xFF440096.toInt(),
        0xFFA1005E.toInt(), 0xFFC70028.toInt(), 0xFFBA0600.toInt(), 0xFF8C1700.toInt(),
        0xFF5C2F00.toInt(), 0xFF104500.toInt(), 0xFF054A00.toInt(), 0xFF00472E.toInt(),
        0xFF004166.toInt(), 0xFF000000.toInt(), 0xFF050505.toInt(), 0xFF050505.toInt(),
        0xFFC7C7C7.toInt(), 0xFF0077FF.toInt(), 0xFF2155FF.toInt(), 0xFF8237FA.toInt(),
        0xFFEB2FB5.toInt(), 0xFFFF2950.toInt(), 0xFFFF2200.toInt(), 0xFFD63200.toInt(),
        0xFFC46200.toInt(), 0xFF358000.toInt(), 0xFF058F00.toInt(), 0xFF008A55.toInt(),
        0xFF0099CC.toInt(), 0xFF212121.toInt(), 0xFF090909.toInt(), 0xFF090909.toInt(),
        0xFFFFFFFF.toInt(), 0xFF0FD7FF.toInt(), 0xFF69A2FF.toInt(), 0xFFD480FF.toInt(),
        0xFFFF45F3.toInt(), 0xFFFF618B.toInt(), 0xFFFF8833.toInt(), 0xFFFF9C12.toInt(),
        0xFFFABC20.toInt(), 0xFF9FE30E.toInt(), 0xFF2BF035.toInt(), 0xFF0CF0A4.toInt(),
        0xFF05FBFF.toInt(), 0xFF5E5E5E.toInt(), 0xFF0D0D0D.toInt(), 0xFF0D0D0D.toInt(),
        0xFFFFFFFF.toInt(), 0xFFA6FCFF.toInt(), 0xFFB3ECFF.toInt(), 0xFFDAABEB.toInt(),
        0xFFFFA8F9.toInt(), 0xFFFFABB3.toInt(), 0xFFFFD2B0.toInt(), 0xFFFFEFA6.toInt(),
        0xFFFFF79C.toInt(), 0xFFD7E895.toInt(), 0xFFA6EDAF.toInt(), 0xFFA2F2DA.toInt(),
        0xFF99FFFC.toInt(), 0xFFDDDDDD.toInt(), 0xFF111111.toInt(), 0xFF111111.toInt()
    )

    override fun read(address: Address): Byte {
        return when (address.value) {
            0x2002 -> { // 0x2002の読み取りで0x2005の書き込み順序がリセットされる
                scrollIndex = 0
                status.read()
            }
            0x2004 -> 0 // TODO impl sprite mem data
            0x2007 -> readInternalMemory()
            else -> error("ppu read: ${address.value.toString(16)} Illegal Access")
        }
    }

    override fun write(address: Address, data: Byte) {
        Log.d("", "${data.toUByte().toString(16)} → 0x${address.value.toString(16)}")
        when (address.value) {
            0x2000 -> ctrlRegister1.data = data
            0x2001 -> ctrlRegister2.data = data
            0x2003 -> spriteMemAddress.value  = data.toInt() and 0xFF
            0x2004 -> {
                val adder = spriteMemAddress.value
                objectAttributeMemory[adder / 4].setData(adder % 4, data)
                spriteMemAddress.increment()
            }
            0x2005 -> {
                if (lineAmount >= 240) {
                    scrollOffset[scrollIndex].fill(data.toInt() and 0xFF)
                } else {
                    scrollOffset[scrollIndex].fill(data.toInt() and 0xFF, lineAmount)
                }
                scrollIndex = (scrollIndex + 1) % 2
            }
            0x2006 -> ppuMemAddress.value = data.toInt() and 0xFF
            0x2007 -> writeToInternalMemory(data)
        }
    }

    fun writeSpriteData(index: Int, data: Byte) = objectAttributeMemory[index / 4].setData(index % 4, data)

    // PPUのパレット以外を読むときはreadSyncBuffer（１サイクル前）の値が返り，代わりにネームテーブル3?のミラーがバッファされる???
    private fun readInternalMemory() : Byte {
        return when (ppuMemAddress.value) {
            in 0x0000 until 0x2000 -> { // パターンテーブル
                readSyncBuffer = cartridge.readChrRom(Address.buildAddress(ppuMemAddress.value))
                readSyncBuffer
            }
            in 0x2000 until 0x23C0, in 0x3000 until 0x33C0 -> { // ネームテーブル0 と　ミラー
                readSyncBuffer = nameTables[0][ppuMemAddress.value % 0x3000 % 0x2000]
                readSyncBuffer
            }
            in 0x23C0 until 0x2400, in 0x33C0 until 0x3400 -> { // 属性テーブル0 と　ミラー
                readSyncBuffer = attributeTables[0][ppuMemAddress.value % 0x33C0 % 0x23C0]
                readSyncBuffer
            }
            in 0x2400 until 0x27C0, in 0x3400 until 0x37C0 -> { // ネームテーブル1 まとめて書いてもいいけど，ミラーリングの実装を待つ
                readSyncBuffer = nameTables[1][ppuMemAddress.value]
                readSyncBuffer
            }
            in 0x27C0 until 0x2800, in 0x37C0 until 0x3800 -> { // 属性テーブル1
                readSyncBuffer = attributeTables[1][ppuMemAddress.value]
                readSyncBuffer
            }
            in 0x2800 until 0x2BC0, in 0x3800 until 0x3BC0 -> { // ネームテーブル2
                readSyncBuffer = nameTables[2][ppuMemAddress.value]
                readSyncBuffer
            }
            in 0x2BC0 until 0x2C00, in 0x3BC0 until 0x3C00 -> { // 属性テーブル2
                readSyncBuffer = attributeTables[2][ppuMemAddress.value]
                readSyncBuffer
            }
            in 0x2C00 until 0x2FC0, in 0x3C00 until 0x3F00 -> { // ネームテーブル3 0x3F00以降のバッファはパレットを呼ぶときに使われる
                readSyncBuffer = nameTables[3][ppuMemAddress.value]
                readSyncBuffer
            }
            in 0x2FC0 until 0x3000 -> { // 属性テーブル3
                readSyncBuffer = attributeTables[3][ppuMemAddress.value]
                readSyncBuffer
            }
            in 0x3F00 until 0x3F10, // バックグラウンドパレット
            in 0x3F20 until 0x3F30, // ミラー
            in 0x3F40 until 0x3F50,
            in 0x3F60 until 0x3F70,
            in 0x3F80 until 0x3F90,
            in 0x3FA0 until 0x3FB0,
            in 0x3FC0 until 0x3FD0,
            in 0x3FE0 until 0x3FF0
            -> {
                bgPaletteTables[(ppuMemAddress.value - 0x3F00) % 0x10 / 4][(ppuMemAddress.value - 0x3F00) % 0x10 % 4]
            }
            in 0x3F10 until 0x3F20,
            in 0x3F30 until 0x3F40,
            in 0x3F50 until 0x3F60,
            in 0x3F70 until 0x3F80,
            in 0x3F90 until 0x3FA0,
            in 0x3FB0 until 0x3FC0,
            in 0x3FD0 until 0x3FE0,
            in 0x3FF0 .. 0x3FFF
            -> { // スプライトパレット TODO $3F00/$3F04/$3F08/$3F0Cのミラーの実装
                chrPaletteTables[(ppuMemAddress.value - 0x3F10) % 0x10 / 4][(ppuMemAddress.value - 0x3F10) % 0x10 % 4]
            }

            else -> error("ppu internal: address out of bounds")
        }
    }

    private fun writeToInternalMemory(data: Byte) {
        Log.d("", "(PPU Internal) ${data.toUByte().toString(16)} → 0x${ppuMemAddress.value.toString(16)}")
        when (ppuMemAddress.value) {
            in 0x2000 until 0x23C0, in 0x3000 until 0x33C0 -> { // ネームテーブル0 と　ミラー TODO ミラーのアドレス処理
                nameTables[0][ppuMemAddress.value % 0x3000 % 0x2000] = data
            }
            in 0x23C0 until 0x2400, in 0x33C0 until 0x3400 -> { // 属性テーブル0 と　ミラー
                attributeTables[0][ppuMemAddress.value % 0x33C0 % 0x23C0] = data
            }
            in 0x2400 until 0x27C0, in 0x3400 until 0x37C0 -> { // ネームテーブル1 まとめて書いてもいいけど，ミラーリングの実装を待つ
                nameTables[1][ppuMemAddress.value % 0x3400 % 0x2400] = data
            }
            in 0x27C0 until 0x2800, in 0x37C0 until 0x3800 -> { // 属性テーブル1
                attributeTables[1][ppuMemAddress.value % 0x37C0 % 0x27C0] = data
            }
            in 0x2800 until 0x2BC0, in 0x3800 until 0x3BC0 -> { // ネームテーブル2
                nameTables[2][ppuMemAddress.value % 0x3800 % 0x2800] = data
            }
            in 0x2BC0 until 0x2C00, in 0x3BC0 until 0x3C00 -> { // 属性テーブル2
                attributeTables[2][ppuMemAddress.value % 0x3BC0 % 0x2BC0] = data
            }
            in 0x2C00 until 0x2FC0, in 0x3C00 until 0x3F00 -> { // ネームテーブル3 0x3F00以降のバッファはパレットを呼ぶときに使われる
                nameTables[3][ppuMemAddress.value % 0x3C00 % 0x2C00] = data
            }
            in 0x2FC0 until 0x3000 -> { // 属性テーブル3
                attributeTables[3][ppuMemAddress.value % 0x2FC0] = data
            }
            in 0x3F00 until 0x3F10, // バックグラウンドパレット
            in 0x3F20 until 0x3F30, // ミラー
            in 0x3F40 until 0x3F50,
            in 0x3F60 until 0x3F70,
            in 0x3F80 until 0x3F90,
            in 0x3FA0 until 0x3FB0,
            in 0x3FC0 until 0x3FD0,
            in 0x3FE0 until 0x3FF0
            -> bgPaletteTables[(ppuMemAddress.value - 0x3F00) % 0x10 / 4][(ppuMemAddress.value - 0x3F00) % 0x10 % 4] = data
            in 0x3F10 until 0x3F20,
            in 0x3F30 until 0x3F40,
            in 0x3F50 until 0x3F60,
            in 0x3F70 until 0x3F80,
            in 0x3F90 until 0x3FA0,
            in 0x3FB0 until 0x3FC0,
            in 0x3FD0 until 0x3FE0,
            in 0x3FF0 .. 0x3FFF
                // TODO $3F00/$3F04/$3F08/$3F0Cのミラーの実装
            -> chrPaletteTables[(ppuMemAddress.value - 0x3F10) % 0x10 / 4][(ppuMemAddress.value - 0x3F10) % 0x10 % 4] = data

            else -> error("ppu internal: ${ppuMemAddress.value.toString(16)} address out of bounds")
        }
        ppuMemAddress.increment()
    }

    private var cycleAmount = 0
    private var lineAmount = 0
    fun run(cycle: Int) {
        cycleAmount += cycle
        if (lineAmount == 239 && cycleAmount / 341 == 240) {
            for (y in 0 until 240) {
                val spritesToRender = oamScan(y) // Object Attribute Memory これから描画するスプライトの情報
                for (x in 0 until 256) {
                    val paletteIndex = getPaletteIndex(x, y)
                    var colorIndex = getPixelData(x, y)
                    var pixelColor = bgPaletteTables[paletteIndex][colorIndex]
                    screen.data[y * 256 + x] = colors[pixelColor.toInt() and 0xFF]
                    if (spritesToRender.isNotEmpty()) {
                        spritesToRender.filter { sprite -> sprite.x <= x && x < sprite.x + 8 }.takeLast(1).forEach {
                            val verticalFlip = (it.attribute.toInt() and 0b1000_0000) != 0
                            val horizontalFlip = (it.attribute.toInt() and 0b0100_0000) != 0
                            val pixelX = if (horizontalFlip) 7 - (x - it.x) else x - it.x
                            val pixelY = if (verticalFlip) 7 - (y - (it.y + 1)) else y - (it.y + 1)
                            colorIndex = getSpriteColorIndex(it.index, pixelX, pixelY)
                            if (colorIndex != 0) { // スプライトのパレットの先頭は透明色なので描画しない
                                pixelColor = chrPaletteTables[it.getColorPaletteIndex()][colorIndex]
                                screen.data[y * 256 + x] = colors[pixelColor.toInt() and 0xFF]
                            }
                        }
                    }
                }
            }
            screen.isReady.postValue(true)
            if (ctrlRegister1.isNMIEnable()) {
                nmi.hasInterrupt = true
            }
            status.setVBlank(true)
        }
        lineAmount = cycleAmount / 341
        if (lineAmount >= 262) {
            lineAmount = 0
            cycleAmount = 0
        }
    }

    private fun oamScan(y: Int) = objectAttributeMemory.filter {
        sprite -> sprite.y >= 0 // y 座標がマイナスのものは描画しない
    }.filter {
        sprite -> sprite.y + 1 <= y &&  y < sprite.y + 1 + ctrlRegister1.getSpriteSize()
    }.take(8) // 1ラインに描画できるスプライトは8個まで


    private fun getNameTableIndex(x: Int, y: Int, scrollX: Int, scrollY: Int) : Int { // x, y座標とスクロールオフセットから対応するネームテーブルの番号を取得する
        return if (ctrlRegister1.getNameTableSelect() <= 1) { // ネームテーブル指定が0, 1のとき
            // ネームテーブル1から右方向にスクロールすると描画範囲はネームテーブル0に戻る
            // (nameTableSelect() + (scrollX + x) / 256) % 2 -> 0 or 1
            // ((scrollY + y) / 240) * 2 -> 0 or 2
            ((ctrlRegister1.getNameTableSelect() + (scrollX + x) / 256) % 2 + ((scrollY + y) / 240) * 2) % 4
        } else { // ネームテーブル指定が2，3のとき
            // 上の計算に最後に2を足して1画面分ネームテーブルをずらす．
            ((ctrlRegister1.getNameTableSelect() + (scrollX + x) / 256) % 2 + ((scrollY + y) / 240) * 2 + 2) % 4
        }
    }

    private fun getPaletteIndex(x: Int, y: Int) : Int {
        val scrollX = scrollOffset[0][y] // y 行目描画時の横方向のオフセット
        val scrollY = scrollOffset[1][y] // y 行目描画時の縦方向のオフセット
        val nameTableIndex = getNameTableIndex(x, y, scrollX, scrollY)
        val palettes = attributeTables[nameTableIndex][((y + scrollY) % 240) / 32 * 8 + ((x + scrollX) % 256) / 32].toInt() and 0xFF
        return if (x % 32 < 16 && y % 32 < 16) {
            (palettes and 0b1100_0000) ushr 6
        } else if (x % 32 >= 16 && y % 32 < 16) {
            (palettes and 0b0011_0000) ushr 4
        } else if (x % 32 < 16 && y % 32 >= 16){
            (palettes and 0b0000_1100) ushr 2
        } else {
            (palettes and 0b0000_0011)
        } and 0b11
    }

    private fun getPixelData(x: Int, y: Int) : Int {
        val scrollX = scrollOffset[0][y] // y 行目描画時の横方向のオフセット
        val scrollY = scrollOffset[1][y] // y 行目描画時の縦方向のオフセット
        val nameTableIndex = getNameTableIndex(x, y, scrollX, scrollY)
        val tileId = nameTables[nameTableIndex][(((y + scrollY) % 240) / 8) * 32 + ((x + scrollX) % 256) / 8].toInt() and 0xFF
        val tileDataAddress : Address = ctrlRegister1.getBGPatternTableAddress()
        tileDataAddress += tileId * 16
        val tileDataOffset = (y % 8).toByte()
        val tileDataLow = cartridge.readChrRom(tileDataAddress + tileDataOffset).toInt() and 0xFF
        val tileDataHigh = cartridge.readChrRom(tileDataAddress + 8 + tileDataOffset).toInt() and 0xFF
        return (((tileDataHigh ushr (7 - x % 8)) and 1) shl 1) or ((tileDataLow ushr (7 - x % 8)) and 1)
    }

    private fun getSpriteColorIndex(spriteIndex: Int, x: Int, y: Int) : Int {
        val tileDataAddress = ctrlRegister1.getSpritePatternTableAddress()
        tileDataAddress += spriteIndex * 8 + y // TODO 大きいサイズのスプライトの処理
        val tileDataLow = cartridge.readChrRom(tileDataAddress).toInt() and 0xFF
        val tileDataHigh = cartridge.readChrRom(tileDataAddress + 8).toInt() and 0xFF
        return (((tileDataHigh ushr (7 - x)) and 1) shl 1) or ((tileDataLow ushr (7 - x)) and 1)
    }
}