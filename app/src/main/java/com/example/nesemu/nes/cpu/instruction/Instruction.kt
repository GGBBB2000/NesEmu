package com.example.nesemu.nes.cpu.instruction

import android.util.Log
import com.example.nesemu.nes.cpu.Register

abstract class Instruction {
    abstract fun exec()
}

data class InstructionInfo(val opcode: Byte, val inst: Instruction, val cycle: Int)
