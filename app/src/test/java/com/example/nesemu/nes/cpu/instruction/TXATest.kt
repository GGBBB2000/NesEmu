package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TXATest {
    private val a = Register.A()
    private val x = Register.X()
    private val p = Register.P()

    @Before
    fun setUp() {
        a.value = 0
        x.value = 0
        p.setFlag(0)
    }

    @Test
    fun `TXA N = false Z = false`() {
        x.value = 1
        TXA(a, x, p).exec()

        assertEquals(x.value, a.value)
        assertFalse(p.negative)
        assertFalse(p.zero)
    }

    @Test
    fun `TXA N = false Z = true`() {
        a.value = 1
        TXA(a, x, p).exec()

        assertEquals(a.value, x.value)
        assertFalse(p.negative)
        assertTrue(p.zero)
    }

    @Test
    fun `TXA N = true Z = false`() {
        x.value = -1
        TXA(a, x, p).exec()

        assertEquals(x.value, a.value)
        assertTrue(p.negative)
        assertFalse(p.zero)
    }
}