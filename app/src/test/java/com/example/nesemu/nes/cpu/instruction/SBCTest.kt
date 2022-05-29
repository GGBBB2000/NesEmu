package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Argument
import com.example.nesemu.nes.cpu.Register
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SBCTest {

    @MockK
    private lateinit var arg: Argument.Readable

    private var a = Register.A()

    private var p = Register.P()

    @Before
    fun before() {
        MockKAnnotations.init(this)
        p.setFlag(0)
    }

    @Test
    fun `N = false V = false Z = false C = false`() {
        every { arg.read() } returns 1
        a.value = 2

        SBC(arg, a, p).exec()

        assertEquals(1.toByte(), a.value)
        assertEquals(0b0010_0000.toByte(), p.toByte())
    }

    @Test
    fun `N = true V = false Z = false C = true`() {
        every { arg.read() } returns 2
        a.value = 1

        SBC(arg, a, p).exec()

        assertEquals((-1).toByte(), a.value)
        assertEquals(0b1010_0001.toByte(), p.toByte())
    }

    @Test
    fun `N = false V = false Z = true C = false`() {
        every { arg.read() } returns 1
        a.value = 1

        SBC(arg, a, p).exec()

        assertEquals(0.toByte(), a.value)
        assertEquals(0b0010_0010.toByte(), p.toByte())
    }

    @Test
    fun `N = false V = true Z = false C = true`() {
        every { arg.read() } returns 1
        a.value = 0x80.toByte()

        SBC(arg, a, p).exec()

        assertEquals(0x7F.toByte(), a.value)
        assertEquals(0b0110_0001.toByte(), p.toByte())
    }

    @Test
    fun `N = true V = true Z = false C = false`() {
        every { arg.read() } returns 0xFF.toByte()
        a.value = 0x7F.toByte()

        SBC(arg, a, p).exec()

        assertEquals(0x80.toByte(), a.value)
        assertEquals(0b1110_0000.toByte(), p.toByte())
    }

    @Test
    fun `N = false V = false Z = false C = false With Carry`() {
        every { arg.read() } returns 0
        a.value = 2
        p.carry = true

        SBC(arg, a, p).exec()

        assertEquals(1.toByte(), a.value)
        assertEquals(0b0010_0000.toByte(), p.toByte())
    }

    @Test
    fun `N = true V = false Z = false C = true With Carry`() {
        every { arg.read() } returns 1
        a.value = 1
        p.carry = true

        SBC(arg, a, p).exec()

        assertEquals((-1).toByte(), a.value)
        assertEquals(0b1010_0001.toByte(), p.toByte())
    }

    @Test
    fun `N = false V = false Z = true C = false With Carry`() {
        every { arg.read() } returns 0
        a.value = 1
        p.carry = true

        SBC(arg, a, p).exec()

        assertEquals(0.toByte(), a.value)
        assertEquals(0b0010_0010.toByte(), p.toByte())
    }

    @Test
    fun `N = false V = true Z = false C = true With Carry`() {
        every { arg.read() } returns 0
        a.value = 0x80.toByte()
        p.carry = true

        SBC(arg, a, p).exec()

        assertEquals(0x7F.toByte(), a.value)
        assertEquals(0b0110_0001.toByte(), p.toByte())
    }

    @Test
    fun `N = true V = true Z = false C = false With Carry`() {
        every { arg.read() } returns 0xFE.toByte()
        a.value = 0x7F.toByte()
        p.carry = true

        SBC(arg, a, p).exec()

        assertEquals(0x80.toByte(), a.value)
        assertEquals(0b1110_0000.toByte(), p.toByte())
    }
}