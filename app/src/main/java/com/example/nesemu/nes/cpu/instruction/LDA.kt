package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register

class LDA(val data: Byte, val a: Register.A, val p: Register.P) : Instruction() {
    override fun exec() {
        a.value = data
        p.negative = a.value.toInt() < 0
        p.zero = a.value == 0.toByte()
    }
}