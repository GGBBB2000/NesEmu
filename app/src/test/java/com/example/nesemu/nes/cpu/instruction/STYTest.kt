package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.cpu.Register
import com.example.nesemu.nes.util.Address
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test

class STYTest {
    @Test
    fun styTest() {
        val y = Register.Y(0xAB.toByte())
        val bus = Bus(mockk(), mockk())
        val dest = Address.buildAddress(0x1234)
        STY(y, dest, bus).exec()
        assertEquals(bus.read(dest), y.value)
    }
}