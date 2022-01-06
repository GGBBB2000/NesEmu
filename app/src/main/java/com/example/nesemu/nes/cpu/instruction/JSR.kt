package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.cpu.Register
import com.example.nesemu.nes.util.Address

class JSR(val pc: Register.PC, val sp: Register.SP, val dest: Address, val bus: Bus) : Instruction() {
    override fun exec() {
        val address = pc.address.value - 1
        val pcHigh = ((address and 0xFF00) ushr 8).toByte()
        val pcLow = (address and 0xFF).toByte()
        bus.write(sp.address--, pcHigh)
        bus.write(sp.address--, pcLow)
        pc.address = dest
    }
}