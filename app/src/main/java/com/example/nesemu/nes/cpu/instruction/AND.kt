package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Argument
import com.example.nesemu.nes.cpu.Register

class AND(private val arg: Argument.Readable, private val a: Register.A, private val p: Register.P) : Instruction() {
    init {
        assert(arg !is Argument.Accumulator)
    }
    override fun exec() {
        a.value = ((a.value.toInt() and arg.read().toInt()) and 0xFF).toByte()
        p.negative = a.value.toInt() < 0
        p.zero = a.value.toInt() == 0
    }
}
