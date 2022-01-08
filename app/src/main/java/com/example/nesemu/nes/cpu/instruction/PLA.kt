package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.cpu.Register

class PLA(val a: Register.A, val sp: Register.SP, val p: Register.P, val bus: Bus) : Instruction() {
    override fun exec() {
        a.value = bus.read(++sp.address)
        p.negative = a.value.toInt() < 0
        p.zero = a.value.toInt() == 0
    }
}