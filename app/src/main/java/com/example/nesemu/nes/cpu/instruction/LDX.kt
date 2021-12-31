package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register

class LDX(val data: Byte, val x: Register.X, val p: Register.P) : Instruction() {
    override fun exec() {
        x.value = data
        p.negative = x.value < 0.toByte()
        p.zero = x.value == 0.toByte()
    }

}