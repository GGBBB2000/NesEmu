package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Argument
import com.example.nesemu.nes.cpu.Register
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.lang.AssertionError

class ANDTest {
    private val a = Register.A()
    private val p = Register.P()
    private val arg: Argument.Readable = mockk()

    @Before
    fun setup() {
        p.setFlag(0)
    }

    // NV1B DIZC
    @Test
    fun `N = false Z = false`() {
        every { arg.read() } returns 0x0A.toByte()
        a.value = 0b0111_0111
        AND(arg, a, p).exec()
        assertEquals(0x02.toByte(), a.value)
        assertEquals(0b0010_0000.toByte(), p.toByte())
    }

    @Test
    fun `N = true Z = false`() {
        every { arg.read() } returns 0xAA.toByte()
        a.value = 0b1000_1101.toByte()
        AND(arg, a, p).exec()
        assertEquals(0x88.toByte(), a.value)
        assertEquals(0b1010_0000.toByte(), p.toByte())
    }

    @Test
    fun `N = false Z = true`() {
        every { arg.read() } returns 0xAA.toByte()
        a.value = 0b0101_0101
        AND(arg, a, p).exec()
        assertEquals(0x0.toByte(), a.value)
        assertEquals(0b0010_0010.toByte(), p.toByte())
    }

    @Test
    fun callWithAccumulator() {
        try {
            AND(Argument.Accumulator(a), a, p)
        } catch (e: AssertionError) {
            // OK
        }
    }
}