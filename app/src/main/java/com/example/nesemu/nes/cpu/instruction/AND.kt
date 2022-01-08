package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register

class AND(val data: Byte, val a: Register.A, val p: Register.P) : Instruction() {
    override fun exec() {
        a.value = ((a.value.toInt() and data.toInt()) and 0xFF).toByte()
        p.negative = a.value.toInt() < 0
        p.zero = a.value.toInt() == 0
    }
}
