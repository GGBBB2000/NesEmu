package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.cpu.Register

class PHP(private val p: Register.P, private val sp: Register.SP, private val bus: Bus): Instruction() {
    override fun exec() {
        TODO("Not yet implemented")
    }
}