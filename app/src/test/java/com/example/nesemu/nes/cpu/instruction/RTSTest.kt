package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.cpu.Register
import com.example.nesemu.nes.util.Address
import org.junit.Assert.*
import org.junit.Test
import io.mockk.*

class RTSTest {
    private val pc = Register.PC(Address.Companion.buildAddress(0))
    private val sp = Register.SP(Address.Companion.buildAddress(0x100))
    private val bus = mockk<Bus>()

    @Test
    fun rtsTest() {
        every { bus.read(any()) } returns 0xAA.toByte()
        RTS(pc, sp, bus).exec()
        assertEquals(0xAAAB, pc.address.value)
        assertEquals(0x102, sp.address.value)
    }
}