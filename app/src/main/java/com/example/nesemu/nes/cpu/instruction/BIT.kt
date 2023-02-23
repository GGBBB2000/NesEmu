package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register

class BIT(private val data: Byte, private val a:Register.A, private val p: Register.P) : Instruction() {
    override fun exec() {
        p.negative = data.toInt() and 0b1000_0000 != 0
        p.overflow = data.toInt() and 0b0100_0000 != 0
        p.zero = data.toInt() and a.value.toInt() == 0
    }
}