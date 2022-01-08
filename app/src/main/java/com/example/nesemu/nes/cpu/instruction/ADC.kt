package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register

class ADC(val data: Byte, val a: Register.A, val p: Register.P) : Instruction() {
    override fun exec() {
        val result = a.value + data + (if (p.carry) 1 else 0)
        if ((((a.value.toInt() xor data.toInt()) and 0x80) == 0)
                    && ((a.value.toInt() xor result) and 0x80 != 0)) {
            p.overflow = true
        }
        p.negative = result < 0
        p.zero = result == 0
        p.carry = result > 0xFF
        a.value = (result and 0xFF).toByte()
    }
}
