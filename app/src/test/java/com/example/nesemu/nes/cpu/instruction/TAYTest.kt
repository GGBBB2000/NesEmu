package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TAYTest {
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
    fun `TAY N = false Z = false`() {
        a.value = 1
        TAY(a, y, p).exec()

        assertEquals(a.value, y.value)
        assertFalse(p.negative)
        assertFalse(p.zero)
    }

    @Test
    fun `TAY N = false Z = true`() {
        y.value = 1
        TAY(a, y, p).exec()

        assertEquals(a.value, y.value)
        assertFalse(p.negative)
        assertTrue(p.zero)
    }

    @Test
    fun `TAY N = true Z = false`() {
        a.value = -1
        TAY(a, y, p).exec()

        assertEquals(a.value, y.value)
        assertTrue(p.negative)
        assertFalse(p.zero)
    }

}