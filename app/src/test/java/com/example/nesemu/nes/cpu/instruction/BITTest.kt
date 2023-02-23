package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class BITTest {
    private val p = Register.P()
    private val a = Register.A()
    private var data: Byte = 0

    init {
        p.setFlag(0b00111101) // N V Z以外のフラグを立てる
    }

    @Before
    fun setup() {
        p.negative = false
        p.overflow = false
        p.zero = false
    }

    @Test
    fun `N = false V = false Z = false`() {
        data = 0x1
        a.value = data
        BIT(data, a, p).exec()
        assertEquals(p.toByte(), 0b0011_1101.toByte())
    }

    @Test
    fun `N = true V = false Z = false`() {
        data = 0b1000_0000.toByte()
        a.value = data
        BIT(data, a, p).exec()
        assertEquals(p.toByte(), 0b1011_1101.toByte())
    }

    @Test
    fun `N = true V = true Z = false`() {
        data = 0b1100_0000.toByte()
        a.value = data
        BIT(data, a, p).exec()
        assertEquals(p.toByte(), 0b1111_1101.toByte())
    }

    @Test
    fun `N = true V = false Z = true`() {
        data = 0b1000_0000.toByte()
        a.value = 1
        BIT(data, a, p).exec()
        assertEquals(p.toByte(), 0b1011_1111.toByte())
    }

    @Test
    fun `N = true V = true Z = true`() {
        data = 0b1100_0000.toByte()
        a.value = 1
        BIT(data, a, p).exec()
        assertEquals(p.toByte(), 0b1111_1111.toByte())
    }

}