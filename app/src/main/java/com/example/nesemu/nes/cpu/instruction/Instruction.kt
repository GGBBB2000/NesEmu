package com.example.nesemu.nes.cpu.instruction

abstract class Instruction {
    abstract fun exec()
}

data class InstructionInfo(val opcode: Byte, val inst: Instruction, val cycle: Int)
