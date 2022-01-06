package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register

class CPX(val data: Byte, val x: Register.X, val p:Register.P) : Instruction() {
    override fun exec() {
        val result = x.value - data
        p.negative = result < 0
        p.zero = result == 0
        p.carry = !p.negative || p.zero
    }
}