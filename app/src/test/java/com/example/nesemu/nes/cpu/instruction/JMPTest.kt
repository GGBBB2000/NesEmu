package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register
import com.example.nesemu.nes.util.Address
import org.junit.Assert.*
import org.junit.Test

class JMPTest {
    private val pc = Register.PC(Address.Companion.buildAddress(0))

    @Test
    fun jumpTest() {
        val dest = Address.buildAddress(0xDEAD)
        JMP(dest, pc).exec()
        assertEquals(dest.value, pc.address.value)
        dest.value = 0xBEEF
        JMP(dest, pc).exec()
        assertEquals(dest.value, pc.address.value)
    }
}