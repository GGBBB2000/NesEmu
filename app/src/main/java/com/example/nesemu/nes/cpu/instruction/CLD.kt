package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register

class CLD(val p: Register.P) : Instruction() {
    override fun exec() {
        p.decimal = false
    }
}