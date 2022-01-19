package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register

class CPY(val data: Byte, val y: Register.Y, val p:Register.P) : Instruction() {
    override fun exec() {
        val result = y.value - data
        p.negative = result < 0
        p.zero = result == 0
        p.carry = !p.negative || p.zero
    }
}