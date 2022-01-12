package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register

class DEX(val x: Register.X, val p: Register.P) : Instruction() {
    override fun exec() {
        x.value--
        p.negative = x.value.toInt() < 0
        p.zero = x.value.toInt() == 0
    }
}