package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register
import org.junit.Assert.*
import org.junit.Test

class DEXTest {
    private val x = Register.X()
    private val p = Register.P()

    // NV1B DIZC
    @Test
    fun `N = false Z = false`() {
        x.value = 2
        DEX(x, p).exec()
        assertEquals(1.toByte(), x.value)
        assertEquals(0b0010_0000.toByte(), p.toByte())
    }

    // NV1B DIZC
    @Test
    fun `N = true Z = false`() {
        x.value = 0
        DEX(x, p).exec()
        assertEquals(0b1111_1111.toByte(), x.value)
        assertEquals(0b1010_0000.toByte(), p.toByte())
    }

    // NV1B DIZC
    @Test
    fun `N = false Z = true`() {
        x.value = 1
        DEX(x, p).exec()
        assertEquals(0.toByte(), 0.toByte())
        assertEquals(0b0010_0010.toByte(), p.toByte())
    }

}
