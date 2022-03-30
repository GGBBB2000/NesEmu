package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class LDXTest {
    private val x = Register.X()
    private val p = Register.P()

    @Before
    fun setup() = p.setFlag(0)

    /*
        flag N,V,1,B,D,I,Z,C = 00100000
        N = false Z = false
     */
    @Test
    fun `N = false Z = false`() {
        LDX(1, x, p).exec()
        assertEquals(p.toByte(), 0b00100000.toByte())
    }

    /*
        flag N,V,1,B,D,I,Z,C = 00100010
        N = false Z = true
     */
    @Test
    fun `N = false Z = true`() {
        LDX(0, x, p).exec()
        assertEquals(p.toByte(), 0b00100010.toByte())
    }

    /*
        flag N,V,1,B,D,I,Z,C = 10100010
        N = true Z = false
     */
    @Test
    fun `N = true Z = false`() {
        LDX(-1, x, p).exec()
        assertEquals(p.toByte(), 0b10100000.toByte())
    }
}