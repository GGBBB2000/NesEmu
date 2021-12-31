package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.cpu.Register
import com.example.nesemu.nes.util.Address

class STA(val a: Register.A, val dest: Address, val bus: Bus) : Instruction() {
    override fun exec() = bus.write(dest, a.value)
}