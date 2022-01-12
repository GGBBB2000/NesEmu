package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register

class TXS(val x: Register.X, val sp: Register.SP) : Instruction() {
    override fun exec() = sp.setAddress(x.value)
}