package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register

class TYA(val a: Register.A, val y: Register.Y, val p: Register.P) : Instruction() {
    override fun exec() {
        a.value = y.value
        p.negative = a.value.toInt() < 0
        p.zero = a.value.toInt() == 0
    }

}