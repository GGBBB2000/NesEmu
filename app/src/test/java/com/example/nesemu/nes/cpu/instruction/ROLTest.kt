package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Argument
import com.example.nesemu.nes.cpu.Register
import io.mockk.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.lang.AssertionError

class ROLTest {
    private val p = Register.P()

    @Before
    fun setup() = p.setFlag(0)

    // NV1B DIZC
    @Test
    fun `N = false Z = false C = false`() {
        val arg: Argument.ReadWritable = mockk {
            every { read() } returns 0x24
            every { write(any()) } just runs
        }
        ROL(arg, p).exec()
        assertEquals(0b0010_0000.toByte(), p.toByte())
        verify (exactly = 1) { arg.write(0x48) }
    }

    @Test
    fun `N = false Z = false C = true`() {
        val arg: Argument.ReadWritable = mockk {
            every { read() } returns 0x81.toByte()
            every { write(any()) } just runs
        }
        ROL(arg, p).exec()
        assertEquals(0b0010_0001.toByte(), p.toByte())
        verify (exactly = 1) { arg.write(0x02) }
    }

    @Test
    fun `N = false Z = true C = true`() {
        val arg: Argument.ReadWritable = mockk {
            every { read() } returns 0x80.toByte()
            every { write(any()) } just runs
        }
        ROL(arg, p).exec()
        assertEquals(0b0010_0011.toByte(), p.toByte())
        verify (exactly = 1) { arg.write(0x0) }
    }

    @Test
    fun `N = true Z = false C = false`() {
        val arg: Argument.ReadWritable = mockk {
            every { read() } returns 0x44.toByte()
            every { write(any()) } just runs
        }
        ROL(arg, p).exec()
        assertEquals(0b1010_0000.toByte(), p.toByte())
        verify (exactly = 1) { arg.write(0x88.toByte()) }
    }

    @Test
    fun `N = true Z = false C = true`() {
        val arg: Argument.ReadWritable = mockk {
            every { read() } returns 0xC4.toByte()
            every { write(any()) } just runs
        }
        ROL(arg, p).exec()
        assertEquals(0b1010_0001.toByte(), p.toByte())
        verify (exactly = 1) { arg.write(0x88.toByte()) }
    }

    @Test
    fun `carry check`() {
        val arg: Argument.ReadWritable = mockk {
            every { read() } returns 0x0.toByte()
            every { write(any()) } just runs
        }
        p.carry = true
        ROL(arg, p).exec()
        assertEquals(0b0010_0000.toByte(), p.toByte())
        verify (exactly = 1) { arg.write(0x1.toByte()) }
    }

    @Test(expected = AssertionError::class)
    fun `Ill arg ZeroPageY`() {
        ROL(mockk<Argument.ZeroPageY>(), p)
    }

    @Test(expected = AssertionError::class)
    fun `Ill arg AbsoluteY`() {
        ROL(mockk<Argument.AbsoluteY>(), p)
    }

    @Test(expected = AssertionError::class)
    fun `Ill arg IndexedIndirect`() {
        ROL(mockk<Argument.IndexedIndirect>(), p)
    }

    @Test(expected = AssertionError::class)
    fun `Ill arg IndirectIndexed`() {
        ROL(mockk<Argument.IndirectIndexed>(), p)
    }
}