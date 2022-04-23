package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.cpu.Register

class PLP(private val p: Register.P, private val sp: Register.SP, private val bus: Bus) : Instruction() {
    override fun exec() = p.setFlag(bus.read(++sp.address))
}