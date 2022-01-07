package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.cpu.Register
import com.example.nesemu.nes.util.Address

class LSR(val a: Register.A, val dest: Address?, val p: Register.P, val bus: Bus) : Instruction() {
    override fun exec() {
        val data = if (dest == null) {
            a.value
        } else {
            bus.read(dest)
        }
        val result = (data.toInt() and 0xFF) ushr 1
        p.carry = (data.toInt() and 1) == 1
        p.negative = result.toByte() < 0
        p.zero = result == 0
        if (dest != null) {
            bus.write(dest, result.toByte())
            return
        }
        a.value = result.toByte()
    }
}