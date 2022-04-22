package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register
import org.junit.Assert.*
import org.junit.Test

class CMPTest {
    private val a = Register.A()
    private val p = Register.P()

    // NV1B DIZC
    // 2 - 1
    @Test
    fun `N = false Z = false C = true`() {
        val data: Byte = 0x1
        a.value = 0x2
        CMP(data, a, p).exec()
        assertEquals(a.value, 0x2.toByte()) // aは計算前と変わらない
        assertEquals(p.toByte(), 0b0010_0001.toByte())
    }

    @Test
    fun `N = false Z = true C = true`() {
        val data: Byte = 0x2
        a.value = 0x2
        CMP(data, a, p).exec()
        assertEquals(a.value, 0x2.toByte()) // aは計算前と変わらない
        assertEquals(p.toByte(), 0b0010_0011.toByte())
    }

    // 1 - 2
    @Test
    fun `N = true Z Z = false C = false`() {
        val data: Byte = 0x2
        a.value = 0x1
        CMP(data, a, p).exec()
        assertEquals(a.value, 0x1.toByte()) // aは計算前と変わらない
        assertEquals(p.toByte(), 0b1010_0000.toByte())
    }
}