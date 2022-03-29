package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register
import com.example.nesemu.nes.util.Address
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TXSTest {
    private val x = Register.X()
    private val sp = Register.SP(Address.Companion.buildAddress(0))

    @Before
    fun setup() {
        x.value = 0
        sp.setAddress(0xFF.toByte())
    }

    @Test
    fun `transfer 0 to sp`() {
        TXS(x, sp).exec()
        assertEquals(0x0100, sp.address.value)
    }

    @Test
    fun `transfer 1 to sp`() {
        x.value = 1
        TXS(x, sp).exec()
        assertEquals(0x0101, sp.address.value)
    }

    @Test
    fun `transfer 0xFF to sp`() {
        x.value = 0xFF.toByte()
        TXS(x, sp).exec()
        assertEquals(0x01FF, sp.address.value)
    }
}