package com.example.nesemu.nes.cpu.instruction

import android.location.Address
import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.cpu.Register

class JSR(pc: Register.PC, sp: Register.SP, dest: Address, bus: Bus) : Instruction() {
    override fun exec() {
        TODO("Not yet implemented")
    }
}