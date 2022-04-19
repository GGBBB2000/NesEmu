package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register
import org.junit.Assert.*
import org.junit.Test

class INYTest {
    private val y = Register.Y()
    private val p = Register.P()

    @Test
    fun `N = false Z = false`() {
        y.value = 0
        INY(y, p).exec()
        assertEquals(1.toByte(), y.value)
        assertEquals(0b0010_0000.toByte(), p.toByte())
    }

    @Test
    fun `N = true Z = false`() {
        y.value = 0x7F
        INY(y, p).exec()
        assertEquals(0b1000_0000.toByte(), y.value)
        assertEquals(0b1010_0000.toByte(), p.toByte())
    }

    @Test
    fun `N = false Z = true`() {
        y.value = 0xFF.toByte()
        INY(y, p).exec()
        assertEquals(0b0000_0000.toByte(), y.value)
        assertEquals(0b0010_0010.toByte(), p.toByte())
    }
}