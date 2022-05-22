package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Argument
import com.example.nesemu.nes.cpu.Register
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LSRTest {

    @MockK
    lateinit var arg: Argument.ReadWritable

    private var p: Register.P = Register.P()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        p.setFlag(0)
    }

    // NV1BDIZC
    @Test
    fun `N = false Z = false C = false`() {
        val result: Byte = 0x5
        every { arg.read() } returns 0xA
        every { arg.write(result) } just runs

        LSR(arg, p).exec()

        assertEquals(0b0010_0000.toByte(), p.toByte())
        verify (exactly = 1){
            arg.read()
            arg.write(result)
        }
    }

    @Test
    fun `N = false Z = false C = true`() {
        val result: Byte = 0x2
        every { arg.read() } returns 0x5
        every { arg.write(result) } just runs

        LSR(arg, p).exec()

        assertEquals(0b0010_0001.toByte(), p.toByte())
        verify (exactly = 1){
            arg.read()
            arg.write(result)
        }
    }

    @Test
    fun `N = false Z = true C = true`() {
        val result: Byte = 0x0
        every { arg.read() } returns 0x1
        every { arg.write(result) } just runs

        LSR(arg, p).exec()

        assertEquals(0b0010_0011.toByte(), p.toByte())
        verify (exactly = 1){
            arg.read()
            arg.write(result)
        }
    }

    @Test
    fun `arg less than 0`() {
        val result: Byte = 0x55
        every { arg.read() } returns 0xAA.toByte()
        every { arg.write(result) } just runs

        LSR(arg, p).exec()

        assertEquals(0b0010_0000.toByte(), p.toByte())
        verify (exactly = 1) {
            arg.read()
            arg.write(result)
        }
    }
}