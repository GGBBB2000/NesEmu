package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.cpu.Register

class RTS(pc: Register.PC, sp: Register.SP, bus: Bus) : Instruction() {
    override fun exec() {
        TODO("Not yet implemented")
    }
}