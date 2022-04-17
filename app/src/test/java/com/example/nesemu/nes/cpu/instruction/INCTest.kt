package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.cpu.Register
import com.example.nesemu.nes.util.Address
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test

class INCTest {

    private val p = Register.P()
    private val bus = Bus(mockk(), mockk())
    private val dest = Address.buildAddress(0)

    @Test
    fun `N = false Z = false`() {
        bus.write(Address.buildAddress(0), 0)
        INC(p, dest, bus).exec()
        assertEquals(1.toByte(), bus.read(dest))
        assertEquals(0b0010_0000.toByte(), p.toByte())
    }

    @Test
    fun `N = true Z = false`() {
        bus.write(Address.buildAddress(0), 0b0111_1111)
        INC(p, dest, bus).exec()
        assertEquals(0b1000_0000.toByte(), bus.read(dest))
        assertEquals(0b1010_0000.toByte(), p.toByte())
    }

    @Test
    fun `N = false Z = true`() {
        bus.write(Address.buildAddress(0), 0b1111_1111.toByte())
        INC(p, dest, bus).exec()
        assertEquals(0b0000_0000.toByte(), bus.read(dest))
        assertEquals(0b0010_0010.toByte(), p.toByte())
    }
}