package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Argument
import com.example.nesemu.nes.cpu.Register

class SBC(private val arg: Argument.Readable,
          private val a: Register.A,
          private val p: Register.P) : Instruction() {

    override fun exec() {
        val input = (arg.read().toInt() + if (p.carry) 1 else 0)
        val result = a.value.toInt() - input
        p.negative = result.toByte() < 0
        p.zero = (result and 0xFF) == 0
        p.carry = a.value.toInt() < input
        p.overflow = ((input < 0 && a.value > 0) && result >= 0) ||
                ((input > 0 && a.value < 0) && result <= 0)
        a.value = result.toByte()
    }
}
