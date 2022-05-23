package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Argument
import com.example.nesemu.nes.cpu.Register
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ASLTest {
    @MockK
    lateinit var arg: Argument.ReadWritable

    private var p: Register.P = Register.P()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        p.setFlag(0)
    }

    @Test
    fun `N = false Z = false C = false`() {
        val result: Byte = 0xA
        every { arg.read() } returns 0x5
        every { arg.write(result) } just runs

        ASL(arg, p).exec()

        assertEquals(0b0010_0000.toByte(), p.toByte())
        verify (exactly = 1){
            arg.read()
            arg.write(result)
        }
    }


    @Test
    fun `N = true Z = false C = false`() {
        val result: Byte = 0x8A.toByte()
        every { arg.read() } returns 0x45
        every { arg.write(result) } just runs

        ASL(arg, p).exec()

        assertEquals(0b1010_0000.toByte(), p.toByte())
        verify (exactly = 1){
            arg.read()
            arg.write(result)
        }
    }

    @Test
    fun `N = false Z = true C = true`() {
        val result: Byte = 0
        every { arg.read() } returns 0x80.toByte()
        every { arg.write(result) } just runs

        ASL(arg, p).exec()

        assertEquals(0b0010_0011.toByte(), p.toByte())
        verify(exactly = 1) {
            arg.read()
            arg.write(result)
        }
    }
}