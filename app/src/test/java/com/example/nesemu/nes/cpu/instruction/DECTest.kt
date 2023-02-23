package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.cpu.Register
import com.example.nesemu.nes.util.Address
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test

class DECTest {
    private val p = Register.P()
    private val dest = Address.buildAddress(0)
    private val bus = Bus(mockk(), mockk())

    // NV1B DIZC
    @Test
    fun `N = false Z = false`() {
        bus.write(dest, 2)
        DEC(p, dest, bus).exec()
        assertEquals(1.toByte(), bus.read(dest))
        assertEquals(0b0010_0000.toByte(), p.toByte())
    }

    // NV1B DIZC
    @Test
    fun `N = true Z = false`() {
        bus.write(dest, 0)
        DEC(p, dest, bus).exec()
        assertEquals(0b1111_1111.toByte(), bus.read(dest))
        assertEquals(0b1010_0000.toByte(), p.toByte())
    }

    // NV1B DIZC
    @Test
    fun `N = false Z = true`() {
        bus.write(dest, 1)
        DEC(p, dest, bus).exec()
        assertEquals(0.toByte(), bus.read(dest))
        assertEquals(0b0010_0010.toByte(), p.toByte())
    }
}