package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register
import kotlin.experimental.xor

class EOR(val data: Byte, val a: Register.A, val p: Register.P) : Instruction() {
    override fun exec() {
        a.value = a.value xor data
        p.negative = a.value < 0
        p.zero = a.value == 0.toByte()
    }
}
