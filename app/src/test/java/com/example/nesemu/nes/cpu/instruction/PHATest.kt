package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.cpu.Register
import com.example.nesemu.nes.util.Address
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test

class PHATest {
    private val bus = Bus(mockk(), mockk())
    private val a = Register.A(0xAB.toByte())
    private val sp = Register.SP(Address.buildAddress(0x1FF))

    @Test
    fun phaTest() {
        PHA(a, sp, bus).exec()
        assertEquals(a.value, 0xAB.toByte()) // aは不変
        assertEquals(sp.address.value, 0x1FE)
        assertEquals(bus.read(Address.buildAddress(0x1FF)), 0xAB.toByte())
    }
}