package com.example.nesemu.nes

import com.example.nesemu.nes.util.Address
import com.example.nesemu.nes.util.IODevice

class JoyPad : IODevice {
    enum class Button(val index: Int) {
        A(0),
        B(1),
        SELECT(2),
        START(3),
        UP(4),
        DOWN(5),
        LEFT(6),
        RIGHT(7)
    }

    private val buttonArray =  Array<Byte>(8) {0} // ボタンの現在の状態を入れる配列
    private val register = Array<Byte>(8) {0} // 現在のボタンの状態をラッチした値を入れる配列

    fun setButtonState(button: Button, isButtonPressed: Boolean) {
        val index = button.index
        buttonArray[index] = if (isButtonPressed) 1 else 0
    }

    private var buttonSelectIndex = 0
    private var isReset = false
    override fun read(address: Address): Byte{
        val ret = register[buttonSelectIndex++]
        buttonSelectIndex %= 8
        return ret
    }

    override fun write(address: Address, data: Byte) {
        if (data.toInt() and 0x1 == 1) {
            this.isReset = true
        } else if (this.isReset && data.toInt() == 0) {
            buttonArray.copyInto(register) // 現在のボタンの状態をラッチする 次にラッチするまで値は不変
            this.isReset = false
        }
    }
}