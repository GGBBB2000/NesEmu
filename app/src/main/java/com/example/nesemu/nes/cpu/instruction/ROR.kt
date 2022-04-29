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
        TODO("Not yet implemented")
    }
}