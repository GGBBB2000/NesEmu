package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register

class BNE(val pc: Register.PC, val p: Register.P, val offset: Byte) : Instruction() {
    override fun exec() {
        if (!p.zero) {
            pc.address += offset.toInt()
        }
    }
}