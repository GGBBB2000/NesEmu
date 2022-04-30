package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Argument
import com.example.nesemu.nes.cpu.Register

class ROR(private val arg: Argument.ReadWritable, private val p: Register.P) : Instruction() {
    init {
        assert(arg !is Argument.ZeroPageY)
        assert(arg !is Argument.AbsoluteY)
        assert(arg !is Argument.IndexedIndirect)
        assert(arg !is Argument.IndirectIndexed)
    }

    override fun exec() {
        val result = ((arg.read().toInt() and 0xFF ) shr 1) or if (p.carry) {
           0b1000_0000
        } else 0
        arg.write(result.toByte())
        p.negative = p.carry
        p.carry = arg.read() % 2 == 1
        p.zero = result == 0
    }
}