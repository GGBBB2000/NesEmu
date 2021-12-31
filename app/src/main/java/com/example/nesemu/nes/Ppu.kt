package com.example.nesemu.nes

import com.example.nesemu.nes.util.Address
import com.example.nesemu.nes.util.IODevice

class Ppu : IODevice {
    val ram = Ram(0x2000)

    data class ctrlRegister1(val data: Byte) {
        fun isNMIEnable() : Boolean = (data.toInt() and  0b1000_0000) != 0

        /**
         * 戻り値 0: 8x8 1: 8x16
         */
        fun getSpriteSize() : Int = data.toInt() and 0b0010_0000 ushr 5

        fun getBGPatternTableAddress() : Address {
            return if ((data.toInt() and 0b0001_0000 ushr 4) == 0) {
                Address.buildAddress(0x00, 0x00)
            } else {
                Address.buildAddress(0x10, 0x00)
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

    data class ctrlRegister2(val data: Byte) {

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
        TODO("Not yet implemented")
    }

    override fun write(address: Address, data: Byte) {
        TODO("Not yet implemented")
    }
}