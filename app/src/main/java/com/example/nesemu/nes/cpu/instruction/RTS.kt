package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.cpu.Register
import com.example.nesemu.nes.util.Address

class RTS(val pc: Register.PC, val sp: Register.SP, val bus: Bus) : Instruction() {
    override fun exec() {
        val low = bus.read(++sp.address)
        val high = bus.read(++sp.address)
        pc.address = Address.buildAddress(high.toInt(), low.toInt())
        pc.address++
    }
}