package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register

class BEQ(val pc: Register.PC, val p: Register.P, private val offset: Byte) : Instruction() {
    override fun exec() {
        if (p.zero) {
            pc.address += offset.toInt()
        }
    }
}