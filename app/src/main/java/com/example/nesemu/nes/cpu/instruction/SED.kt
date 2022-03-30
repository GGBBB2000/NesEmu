package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register

class SED(val p: Register.P) : Instruction() {
    override fun exec() {
        p.decimal = true
    }
}