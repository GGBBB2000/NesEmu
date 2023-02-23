package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Argument
import com.example.nesemu.nes.cpu.Register

class ADC(val arg: Argument.Readable, val a: Register.A, val p: Register.P) : Instruction() {
    override fun exec() {
        val result = a.value.toInt().and(0xFF) + arg.read().toInt().and(0xFF) + (if (p.carry) 1 else 0)
        if ((((a.value.toInt() xor arg.read().toInt()) and 0x80) == 0)
                    && ((a.value.toInt() xor result) and 0x80 != 0)) {
            p.overflow = true
        }
        p.negative = result.toByte() < 0
        p.zero = result.toByte() == 0.toByte()
        p.carry = result.and(0x100) > 0
        a.value = result.toByte()
    }
}
