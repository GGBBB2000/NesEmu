package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.cpu.Register
import com.example.nesemu.nes.util.Address

class DEC(val p: Register.P, val dest: Address, val bus: Bus) : Instruction() {
    override fun exec() {
        val result = bus.read(dest) - 1
        p.negative = result < 0
        p.zero = result == 0
        bus.write(dest, result.toByte())
    }
}