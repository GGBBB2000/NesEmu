package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register

class BRK(private val p: Register.P): Instruction() {
    override fun exec() = if (p.interrupt) {
        // Ignore BRK
    } else {
        p.breakFlag = true
    }
}