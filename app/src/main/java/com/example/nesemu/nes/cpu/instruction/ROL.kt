package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Argument
import com.example.nesemu.nes.cpu.Register

class ROL(private val arg: Argument.ReadWritable, val p: Register.P) : Instruction() {
    override fun exec() {
        var result = (arg.read().toInt() and 0xFF) shl 1
        if (p.carry) {
            result++
        }
        p.carry = result > 0xFF
        p.negative = result.toByte() < 0
        p.zero = result and 0xFF == 0
        arg.write(result.toByte())
    }
}