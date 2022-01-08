package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register

class CLC(val p: Register.P) : Instruction() {
    override fun exec() {
        p.carry = false
    }
}