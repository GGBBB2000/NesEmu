package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Argument
import com.example.nesemu.nes.cpu.Register

class ASL(private val arg: Argument.ReadWritable, private val p: Register.P) : Instruction() {
    override fun exec() {
        val input = arg.read().toInt()
        val result = ((input shl 1) and 0xFF).toByte()
        p.negative = result.toInt() < 0
        p.zero = result.toInt() and 0xFF == 0
        p.carry = input < 0
        arg.write(result)
    }
}