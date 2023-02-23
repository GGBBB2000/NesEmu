package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.cpu.Register
import com.example.nesemu.nes.util.Address
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PLATest {
    private val a = Register.A()
    private val sp = Register.SP(Address.buildAddress(0x1FF))
    private val p = Register.P()
    private val bus = Bus(mockk(), mockk())

    @Before
    fun setup() {
        sp.address.value = 0x1FF
    }

    // NV1B DIZC
    @Test
    fun `N = false Z = false`() {
        bus.write(sp.address--, 0x01.toByte())
        PLA(a, sp, p, bus).exec()
        assertEquals(a.value, 0x01.toByte())
        assertEquals(sp.address.value, 0x1FF)
        assertEquals(p.toByte(), 0b0010_0000.toByte())
    }

    @Test
    fun `N = true Z = false`() {
        bus.write(sp.address--, 0xAB.toByte())
        PLA(a, sp, p, bus).exec()
        assertEquals(a.value, 0xAB.toByte())
        assertEquals(sp.address.value, 0x1FF)
        assertEquals(p.toByte(), 0b1010_0000.toByte())
    }

    @Test
    fun `N = false Z = true`() {
        bus.write(sp.address--, 0x0.toByte())
        PLA(a, sp, p, bus).exec()
        assertEquals(a.value, 0x0.toByte())
        assertEquals(sp.address.value, 0x1FF)
        assertEquals(p.toByte(), 0b0010_0010.toByte())
    }
}