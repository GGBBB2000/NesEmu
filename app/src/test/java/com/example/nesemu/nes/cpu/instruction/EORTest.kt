package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Argument
import com.example.nesemu.nes.cpu.Register
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.lang.AssertionError

class EORTest {
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
        a.value = 0b0111_1111
        EOR(arg, a, p).exec()
        assertEquals(0x75.toByte(), a.value)
        assertEquals(0b0010_0000.toByte(), p.toByte())
    }

    @Test
    fun `N = true Z = false`() {
        a.value = 0b1000_1111.toByte()
        EOR(arg, a, p).exec()
        assertEquals(0x85.toByte(), a.value)
        assertEquals(0b1010_0000.toByte(), p.toByte())
    }

    @Test
    fun `N = false Z = true`() {
        a.value = 0b0000_1010
        EOR(arg, a, p).exec()
        assertEquals(0x0.toByte(), a.value)
        assertEquals(0b0010_0010.toByte(), p.toByte())
    }

    @Test
    fun callWithAccumulator() {
        try {
            EOR(Argument.Accumulator(a), a, p)
        } catch (e: AssertionError) {
            // OK
        }
    }
}