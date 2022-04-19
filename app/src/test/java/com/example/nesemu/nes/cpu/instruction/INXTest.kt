package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register
import org.junit.Assert.*
import org.junit.Test

class INXTest {
    private val x = Register.X()
    private val p = Register.P()

    @Test
    fun `N = false Z = false`() {
        x.value = 0
        INX(x, p).exec()
        assertEquals(1.toByte(), x.value)
        assertEquals(0b0010_0000.toByte(), p.toByte())
    }

    @Test
    fun `N = true Z = false`() {
        x.value = 0x7F
        INX(x, p).exec()
        assertEquals(0b1000_0000.toByte(), x.value)
        assertEquals(0b1010_0000.toByte(), p.toByte())
    }

    @Test
    fun `N = false Z = true`() {
        x.value = 0xFF.toByte()
        INX(x, p).exec()
        assertEquals(0b0000_0000.toByte(), x.value)
        assertEquals(0b0010_0010.toByte(), p.toByte())
    }
}