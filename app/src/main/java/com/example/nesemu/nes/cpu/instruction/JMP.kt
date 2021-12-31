package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register
import com.example.nesemu.nes.util.Address

class JMP(val dest: Address, val pc: Register.PC) : Instruction() {
    override fun exec() {
        pc.address = dest
    }
}