package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Argument
import com.example.nesemu.nes.cpu.Register
import kotlin.experimental.xor

class EOR(private val arg: Argument.Readable, private val a: Register.A, private val p: Register.P)
    : Instruction() {
    init {
        assert(arg !is Argument.Accumulator)
    }
    override fun exec() {
        a.value = a.value xor arg.read()
        p.negative = a.value < 0
        p.zero = a.value == 0.toByte()
    }
}
