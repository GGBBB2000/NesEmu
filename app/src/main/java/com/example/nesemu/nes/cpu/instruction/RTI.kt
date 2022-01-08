package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.cpu.Register
import com.example.nesemu.nes.util.Address

class RTI(val pc: Register.PC, val sp: Register.SP,  val p: Register.P, val bus: Bus) : Instruction(){
    override fun exec() {
        p.setFlag(bus.read(++sp.address))
        val pcLow = bus.read(++sp.address)
        val pcHigh = bus.read(++sp.address)
        pc.address = Address.buildAddress(pcHigh.toInt(), pcLow.toInt())
    }
}