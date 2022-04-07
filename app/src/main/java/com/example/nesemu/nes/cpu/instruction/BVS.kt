package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register

class BVS(val pc: Register.PC, val p: Register.P, val offset: Byte) : Instruction() {
    override fun exec() {
        if (p.overflow) {
            pc.address += offset.toInt()
        }
    }
}