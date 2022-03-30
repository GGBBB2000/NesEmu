package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register

class CLV(val p: Register.P) : Instruction() {
    override fun exec() {
        p.overflow = false
    }
}