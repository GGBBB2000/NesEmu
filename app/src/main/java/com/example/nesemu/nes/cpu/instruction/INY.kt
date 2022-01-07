package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register

class INY(val y: Register.Y, val p: Register.P) : Instruction() {
    override fun exec() {
        y.value++
        p.negative = y.value.toInt() < 0
        p.zero = y.value.toInt() == 0
    }
}