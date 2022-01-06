package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.cpu.Register
import com.example.nesemu.nes.util.Address

class STY(val y: Register.Y, val dest: Address, val bus: Bus) : Instruction() {
    override fun exec() = bus.write(dest, y.value)
}