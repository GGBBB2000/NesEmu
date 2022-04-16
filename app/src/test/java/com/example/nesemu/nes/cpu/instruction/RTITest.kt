package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.cpu.Register
import com.example.nesemu.nes.util.Address
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test

class RTITest {
    private val pc = Register.PC(Address.buildAddress(0))
    private val p = Register.P()
    private val sp = Register.SP(Address.buildAddress(0x100))

    @MockK
    private val bus = Bus(mockk(), mockk())

    @Test
    fun rtiTest() {
        // setup
        val address = Address.buildAddress(0x101)
        bus.write(address, 0b10101011.toByte()) // ステータスレジスタ
        bus.write(address + 1, 0xAA.toByte()) // PC下位バイト
        bus.write(address + 2, 0xBB.toByte()) // PC上位バイト

        RTI(pc, sp, p, bus).exec()

        assertEquals(0b10101011.toByte(), p.toByte())
        assertEquals(0xBBAA, pc.address.value)
    }
}