package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Argument
import com.example.nesemu.nes.cpu.Register
import io.mockk.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.lang.AssertionError

class RORTest {
    private val p = Register.P()

    @Before
    fun setup() = p.setFlag(0)

    // NV1B DIZC
    @Test
    fun `N = false Z = false C = false`() {
        val arg: Argument.ReadWritable = mockk {
            every { read() } returns 0x44
            every { write(any()) } just runs
        }
        ROR(arg, p).exec()
        assertEquals(0b0010_0000.toByte(), p.toByte())
        verify (exactly = 1) { arg.write(0x22) }
    }

    @Test
    fun `N = false Z = false C = true`() {
        val arg: Argument.ReadWritable = mockk {
            every { read() } returns 0x23
            every { write(any()) } just runs
        }
        ROR(arg, p).exec()
        assertEquals(0b0010_0001.toByte(), p.toByte())
        verify (exactly = 1) { arg.write(0x11) }
    }

    @Test
    fun `N = false Z = true C = true`() {
        val arg: Argument.ReadWritable = mockk {
            every { read() } returns 0x1
            every { write(any()) } just runs
        }
        ROR(arg, p).exec()
        assertEquals(0b0010_0011.toByte(), p.toByte())
        verify (exactly = 1) { arg.write(0x0) }
    }

    @Test
    fun `N = true Z = false C = false`() {
        val arg: Argument.ReadWritable = mockk {
            every { read() } returns 0xCC.toByte()
            every { write(any()) } just runs
        }
        p.carry = true
        ROR(arg, p).exec()
        assertEquals(0b1010_0000.toByte(), p.toByte())
        verify (exactly = 1) { arg.write(0xE6.toByte()) }
    }

    @Test
    fun `N = true Z = false C = true`() {
        val arg: Argument.ReadWritable = mockk {
            every { read() } returns 0x55.toByte()
            every { write(any()) } just runs
        }
        p.carry = true
        ROR(arg, p).exec()
        assertEquals(0b1010_0001.toByte(), p.toByte())
        verify (exactly = 1) { arg.write(0xAA.toByte()) }
    }

    @Test(expected = AssertionError::class)
    fun `Ill arg ZeroPageY`() {
        ROR(mockk<Argument.ZeroPageY>(), p)
    }

    @Test(expected = AssertionError::class)
    fun `Ill arg AbsoluteY`() {
        ROR(mockk<Argument.AbsoluteY>(), p)
    }

    @Test(expected = AssertionError::class)
    fun `Ill arg IndexedIndirect`() {
        ROR(mockk<Argument.IndexedIndirect>(), p)
    }

    @Test(expected = AssertionError::class)
    fun `Ill arg IndirectIndexed`() {
        ROR(mockk<Argument.IndirectIndexed>(), p)
    }
}