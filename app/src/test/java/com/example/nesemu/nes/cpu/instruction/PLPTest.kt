package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.cpu.Register
import com.example.nesemu.nes.util.Address
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test

class PLPTest {
    private val p = Register.P()
    private val sp = Register.SP(Address.buildAddress(0x1FE))
    private val bus = Bus(mockk(), mockk())

    @Test
    fun testPLP() {
        bus.write(Address.buildAddress(0x1FF), 0b1010_1010.toByte())
        PLP(p, sp, bus).exec()
        assertEquals(p.toByte(), 0b1010_1010.toByte())
        assertEquals(sp.address.value, 0x1FF)
    }
}