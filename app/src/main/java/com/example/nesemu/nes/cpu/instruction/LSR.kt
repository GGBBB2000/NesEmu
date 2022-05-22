package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Argument
import com.example.nesemu.nes.cpu.Register

class LSR(private val arg: Argument.ReadWritable, private val p: Register.P) : Instruction() {
    override fun exec() {
        val data = arg.read()
        val result = (data.toInt() and 0xFF) ushr 1
        p.carry = (data.toInt() and 1) == 1
        p.negative = result.toByte() < 0
        p.zero = result == 0
        arg.write(result.toByte())
    }
}