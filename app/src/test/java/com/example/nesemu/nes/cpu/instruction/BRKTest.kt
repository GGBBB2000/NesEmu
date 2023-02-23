package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class BRKTest {
    private val p = Register.P()

    @Before
    fun setup() = p.setFlag(0)

    @Test
    fun `I = false`() {
        p.interrupt = false
        BRK(p).exec()
        assertTrue(p.breakFlag)
    }

    @Test
    fun `I = true`() {
        p.interrupt = true
        BRK(p).exec()
        assertFalse(p.breakFlag)
    }
}