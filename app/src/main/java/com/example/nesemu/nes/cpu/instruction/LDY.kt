package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register

class LDY(val data: Byte, val y: Register.Y, val p: Register.P) : Instruction() {
    override fun exec() {
        y.value = data
        p.negative = (y.value.toInt() and 0xFF) < 0
        p.zero = y.value.toInt() == 0
    }

}