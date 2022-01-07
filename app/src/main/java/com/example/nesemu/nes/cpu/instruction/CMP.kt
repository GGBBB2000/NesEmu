package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register

class CMP(val data: Byte, val a: Register.A, val p:Register.P) : Instruction() {
    override fun exec() {
        val result = a.value - data
        p.negative = result < 0
        p.zero = result == 0
        p.carry = !p.negative || p.zero
    }
}