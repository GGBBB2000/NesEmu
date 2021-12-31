package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register

class SEI(val p: Register.P) : Instruction() {
    override fun exec() {
        p.interrupt = true
    }
}