package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Argument
import com.example.nesemu.nes.cpu.Register
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.lang.AssertionError

class ORATest {
    private val a = Register.A()
    private val p = Register.P()
    private val arg: Argument.Readable = mockk {
        every { read() } returns 0x0A.toByte()
    }

    @Before
    fun setup() {
        p.setFlag(0)
    }

    // NV1B DIZC
    @Test
    fun `N = false Z = false`() {
        a.value = 0b0010_0011
        ORA(arg, a, p).exec()
        assertEquals(0x2B.toByte(), a.value)
        assertEquals(0b0010_0000.toByte(), p.toByte())
    }

    @Test
    fun `N = true Z = false`() {
        a.value = 0b1000_0100.toByte()
        ORA(arg, a, p).exec()
        assertEquals(0x8E.toByte(), a.value)
        assertEquals(0b1010_0000.toByte(), p.toByte())
    }

    @Test
    fun `N = false Z = true`() {
        every { arg.read() } returns 0
        a.value = 0
        ORA(arg, a, p).exec()
        assertEquals(0x0.toByte(), a.value)
        assertEquals(0b0010_0010.toByte(), p.toByte())
    }

    @Test
    fun callWithAccumulator() {
        try {
            ORA(Argument.Accumulator(a), a, p)
        } catch (e: AssertionError) {
           // OK
        }
    }
}