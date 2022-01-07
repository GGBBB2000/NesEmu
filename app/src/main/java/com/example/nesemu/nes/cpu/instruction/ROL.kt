package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.cpu.Register
import com.example.nesemu.nes.util.Address

class ROL(val a: Register.A, val dest: Address?, val p: Register.P, val bus: Bus) : Instruction() {
    override fun exec() {
        val data = if (dest == null) {
            a.value
        } else {
            bus.read(dest)
        }
        var result = (data.toInt() and 0xFF) shl 1
        if (p.carry) {
            result++
        }
        p.carry = result > 0xFF
        p.negative = result.toByte() < 0
        p.zero = result and 0xFF == 0
        if (dest != null) {
            bus.write(dest, result.toByte())
            return
        }
        a.value = result.toByte()
    }
}