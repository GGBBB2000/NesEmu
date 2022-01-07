package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.cpu.Register

class PHA(val a: Register.A, val sp: Register.SP, val bus: Bus) : Instruction() {
    override fun exec() = bus.write(sp.address--, a.value)
}