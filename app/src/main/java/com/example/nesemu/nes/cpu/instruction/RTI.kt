package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.cpu.Register

class RTI(pc: Register.PC, sp: Register.SP,  p: Register.P, bus: Bus) : Instruction(){
    override fun exec() {
        TODO("Not yet implemented")
    }
}