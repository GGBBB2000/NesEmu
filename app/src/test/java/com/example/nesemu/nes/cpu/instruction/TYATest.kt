package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TYATest {
    private val a = Register.A()
    private val y = Register.Y()
    private val p = Register.P()

    @Before
    fun setUp() {
        a.value = 0
        y.value = 0
        p.setFlag(0)
    }

    @Test
    fun `TYA N = false Z = false`() {
        y.value = 1
        TYA(a, y, p).exec()

        assertEquals(y.value, a.value)
        assertFalse(p.negative)
        assertFalse(p.zero)
    }

    @Test
    fun `TYA N = false Z = true`() {
        a.value = 1
        TYA(a, y, p).exec()

        assertEquals(y.value, a.value)
        assertFalse(p.negative)
        assertTrue(p.zero)
    }

    @Test
    fun `TYA N = true Z = false`() {
        y.value = -1
        TYA(a, y, p).exec()

        assertEquals(y.value, a.value)
        assertTrue(p.negative)
        assertFalse(p.zero)
    }
}