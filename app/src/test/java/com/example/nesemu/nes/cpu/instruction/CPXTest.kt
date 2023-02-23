package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register
import org.junit.Assert.*
import org.junit.Test

class CPXTest {
    private val x = Register.X()
    private val p = Register.P()

    // NV1B DIZC
    // 2 - 1
    @Test
    fun `N = false Z = false C = true`() {
        val data: Byte = 0x1
        x.value = 0x2
        CPX(data, x, p).exec()
        assertEquals(x.value, 0x2.toByte()) // xは計算前と変わらない
        assertEquals(p.toByte(), 0b0010_0001.toByte())
    }

    @Test
    fun `N = false Z = true C = true`() {
        val data: Byte = 0x2
        x.value = 0x2
        CPX(data, x, p).exec()
        assertEquals(x.value, 0x2.toByte()) // xは計算前と変わらない
        assertEquals(p.toByte(), 0b0010_0011.toByte())
    }

    // 1 - 2
    @Test
    fun `N = true Z Z = false C = false`() {
        val data: Byte = 0x2
        x.value = 0x1
        CPX(data, x, p).exec()
        assertEquals(x.value, 0x1.toByte()) // xは計算前と変わらない
        assertEquals(p.toByte(), 0b1010_0000.toByte())
    }
}