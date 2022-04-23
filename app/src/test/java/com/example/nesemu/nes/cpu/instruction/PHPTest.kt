package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.cpu.Register
import com.example.nesemu.nes.util.Address
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test

class PHPTest {
    private val p = Register.P()
    private val sp = Register.SP(Address.buildAddress(0x1FF))
    private val bus = Bus(mockk(), mockk())

    // NV1B DIZC
    @Test
    fun testPHP() {
        p.setFlag(0b1010_1010.toByte())
        PHP(p, sp, bus).exec()
        assertEquals(p.toByte(), 0b1010_1010.toByte())
        assertEquals(sp.address.value, 0x1FE)
        assertEquals(bus.read(Address.buildAddress(0x1FF)), 0b1010_1010.toByte())
    }
}