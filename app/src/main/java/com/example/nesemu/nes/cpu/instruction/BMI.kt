package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register

class BMI(val pc: Register.PC, val p: Register.P, private val offset: Byte) : Instruction() {
    override fun exec() {
        if (p.negative) {
            pc.address += offset.toInt()
        }
    }
}