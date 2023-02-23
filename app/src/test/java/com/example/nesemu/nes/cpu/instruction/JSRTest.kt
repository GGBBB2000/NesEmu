package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.cpu.Register
import com.example.nesemu.nes.util.Address
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test

class JSRTest {
    private val pc = Register.PC(Address.buildAddress(0x3210))
    private val bus = Bus(mockk(), mockk())
    private val dest = Address.buildAddress(0x1234)
    private val sp = Register.SP(Address.buildAddress(0x1FF))

    @Test
    fun jsrTest() {
        JSR(pc, sp, dest, bus).exec()
        assertEquals(pc.address.value, 0x1234)
        assertEquals(bus.read(Address.buildAddress(sp.address.value + 1)), 0x0F.toByte()) // PC下位バイト JSRの復帰先アドレスは次の命令の一つ前のアドレス
        assertEquals(bus.read(Address.buildAddress(sp.address.value + 2)), 0x32.toByte()) // PC上位バイト
    }
}