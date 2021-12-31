package com.example.nesemu.nes.cpu.instruction

import android.location.Address
import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.cpu.Register

class INX(val x: Register.X, val p: Register.P) : Instruction() {
    override fun exec() {
        x.value++
        p.negative = (x.value.toInt() and 0xFF) < 0
        p.zero = x.value.toInt() == 0
    }
}