package com.example.nesemu.nes.cpu.instruction

import android.location.Address
import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.cpu.Register

class TXS(val x: Register.X, val sp: Register.SP) : Instruction() {
    override fun exec() = sp.setAddress(x.value)
}