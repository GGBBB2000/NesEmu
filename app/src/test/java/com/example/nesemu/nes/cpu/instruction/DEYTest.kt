package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register
import org.junit.Assert.*
import org.junit.Test

class DEYTest {
    private val y = Register.Y()
    private val p = Register.P()

    // NV1B DIZC
    @Test
    fun `N = false Z = false`() {
        y.value = 2
        DEY(y, p).exec()
        assertEquals(1.toByte(), y.value)
        assertEquals(0b0010_0000.toByte(), p.toByte())
    }

    // NV1B DIZC
    @Test
    fun `N = true Z = false`() {
        y.value = 0
        DEY(y, p).exec()
        assertEquals(0b1111_1111.toByte(), y.value)
        assertEquals(0b1010_0000.toByte(), p.toByte())
    }

    // NV1B DIZC
    @Test
    fun `N = false Z = true`() {
        y.value = 1
        DEY(y, p).exec()
        assertEquals(0.toByte(), 0.toByte())
        assertEquals(0b0010_0010.toByte(), p.toByte())
    }

}
