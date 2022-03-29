package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register

class TSX(val x: Register.X, val sp: Register.SP, val p: Register.P) : Instruction() {
    override fun exec() {
        x.value = sp.address.value.toByte()
        p.negative = x.value < 0
        p.zero = x.value.toInt() == 0
    }

}