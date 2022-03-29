package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TAXTest {

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
    fun `TAX N = false Z = false`() {
        a.value = 1
        TAX(a, x, p).exec()

        assertEquals(a.value, x.value)
        assertFalse(p.negative)
        assertFalse(p.zero)
    }

    @Test
    fun `TAX N = false Z = true`() {
        x.value = 1
        TAX(a, x, p).exec()

        assertEquals(a.value, x.value)
        assertFalse(p.negative)
        assertTrue(p.zero)
    }

    @Test
    fun `TAX N = true Z = false`() {
        a.value = -1
        TAX(a, x, p).exec()

        assertEquals(a.value, x.value)
        assertTrue(p.negative)
        assertFalse(p.zero)
    }
}