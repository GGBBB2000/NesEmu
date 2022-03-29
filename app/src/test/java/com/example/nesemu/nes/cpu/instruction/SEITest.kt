package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

internal class SEITest {

    private val p = Register.P()

    @Before
    fun setUp() = p.setFlag(0)

    @Test
    fun setI() {
        SEI(p).exec()
        assertTrue(p.interrupt)
    }

    @Test
    fun `set I with I == true`() {
        p.interrupt = true
        SEI(p).exec()
        assertTrue(p.interrupt)
    }
}