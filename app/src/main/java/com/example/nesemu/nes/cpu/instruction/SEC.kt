package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register

class SEC(val p: Register.P) : Instruction() {
    override fun exec() {
        p.carry = true
    }
}