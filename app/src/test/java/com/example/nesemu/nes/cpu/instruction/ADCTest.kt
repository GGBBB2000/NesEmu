package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Argument
import com.example.nesemu.nes.cpu.Register
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

internal class ADCTest {

    lateinit var a: Register.A
    lateinit var p: Register.P

    @MockK
    lateinit var arg: Argument.Readable

    @Before
    fun setup() {
        a = Register.A()
        p = Register.P()
        MockKAnnotations.init(this)
    }

    private fun execTest(argVal: Byte,
                         aVal: Byte,
                         ansA: Byte,
                         n: Boolean = false,
                         v: Boolean = false,
                         z: Boolean = false,
                         c: Boolean = false) {
        every { arg.read() } returns argVal
        a.value = aVal
        ADC(arg, a, p).exec()

        val ansP = Register.P().apply {
            zero = z
            overflow = v
            negative = n
            carry = c
        }
        validate(ansA, ansP.toByte())
    }

    // NV1BDIZC
    private fun validate(ansA: Byte, ansP: Byte) {
        assertEquals(ansA, a.value)
        assertEquals(ansP, p.toByte())
    }

    // 0000
    @Test
    fun `N = 0 V = 0 Z = 0 C = 0`() = execTest(1, 1, 2)

    // 0001
    @Test
    fun `N = 0 V = 0 Z = 0 C = 1`() = execTest(0xFF.toByte(), 0x2, 1, c = true)

    // 0010
    @Test
    fun `N = 0 V = 0 Z = 1 C = 0`() = execTest(0, 0, 0, z = true)

    // 0011
    @Test
    fun `N = 0 V = 0 Z = 1 C = 1`() {
        execTest(0xFF.toByte(), 1, 0, z = true, c = true)
    }

    // 0100 結果が正のとき c = true 負のとき n = true

    // 0101
    @Test
    fun `N = 0 V = 1 Z = 0 C = 1`() {
        execTest(0x80.toByte(), 0x81.toByte(), 1, v = true, c = true)
    }

    // 0110 z = true v = true のとき キャリーが必ず発生

    // 0111
    @Test
    fun `N = 0 V = 1 Z = 1 C = 1`() {
        execTest(0x80.toByte(), 0x80.toByte(), 0, v = true, z = true, c = true)
    }

    // 1000
    @Test
    fun `N = 1 V = 0 Z = 0 C = 0`() {
        execTest(0x80.toByte(), 1, 0x81.toByte(), n = true)
    }

    // 1001
    @Test
    fun `N = 1 V = 0 Z = 0 C = 1`() {
       execTest(0xC0.toByte(), 0xC0.toByte(), 0x80.toByte(), n = true, c = true)
    }

    // 1010 N = true && Z = true は成り立たない
    // 1011 同上

    // 1100
    @Test
    fun `N = 1 V = 1 Z = 0 C = 0`() {
        execTest(0x7F, 0x01, 0x80.toByte(), n = true, v = true)
    }

    // 1101 正 + 正でキャリーは発生しない
    // 1110 正 + 正では0にならない
    // 1111 N = Z = trueにはならない

    // キャリーありの演算
    @Test
    fun withCarry() {
        p.carry = true
        execTest(1, 1, 3)
    }
}