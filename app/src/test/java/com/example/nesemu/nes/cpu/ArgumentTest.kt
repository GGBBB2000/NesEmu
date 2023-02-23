package com.example.nesemu.nes.cpu

import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.util.Address
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ArgumentTest {

    lateinit var bus: Bus
    var x: Register.X = Register.X()
    var y: Register.Y = Register.Y()

    @Before
    fun resetBus()  {
        bus = Bus(mockk(), mockk())
        x.value = 0
        y.value = 0
    }

    @Test
    fun `Implied Test`() = assertEquals("", Argument.Implied.toString())

    @Test
    fun `Accumulator Test`() {
        val a = Register.A()
        val accumulator = Argument.Accumulator(a)
        accumulator.write(0xAB.toByte())
        assertEquals(0xAB.toByte(), accumulator.read())
        assertEquals("A", accumulator.toString())
    }

    @Test
    fun `Immediate Test`() {
        val immediate = Argument.Immediate(0xAB.toByte())
        assertEquals(0xAB.toByte(), immediate.read())
        assertEquals("#\$AB", immediate.toString())
    }

    @Test
    fun `ZeroPage Test`() {
        val zeroPageAddress = Address.buildAddress(0x000B)
        val zeroPage = Argument.ZeroPage(0x0B.toByte(), bus)
        zeroPage.write(0xBA.toByte())
        assertEquals(bus.read(zeroPageAddress), zeroPage.read())
        assertEquals("\$0B", zeroPage.toString())
    }

    @Test
    fun `ZeroPageX Test`() {
        x.value = 0x3
        val zeroPageAddress = Address.buildAddress(0x000A)
        val zeroPageX = Argument.ZeroPageX(0x0A.toByte(), x, bus)
        zeroPageX.write(0xBA.toByte())
        assertEquals(bus.read(zeroPageAddress + x.value), zeroPageX.read())
        assertEquals("\$0A,X", zeroPageX.toString())
    }

    @Test
    fun `ZeroPageY Test`() {
        y.value = 0x5
        val zeroPageAddress = Address.buildAddress(0x000C)
        val zeroPageY = Argument.ZeroPageY(0x0C.toByte(), y, bus)
        zeroPageY.write(0xBA.toByte())
        assertEquals(bus.read(zeroPageAddress + y.value), zeroPageY.read())
        assertEquals("\$0C,Y", zeroPageY.toString())
    }

    @Test
    fun `Absolute Test`() {
        val address = Address.buildAddress(0x0700)
        val absolute = Argument.Absolute(address, bus)
        absolute.write(0xFA.toByte())
        assertEquals(bus.read(address), absolute.read())
        assertEquals("$0700", absolute.toString())
    }

    @Test
    fun `AbsoluteX Test`() {
        x.value = 0x4
        val address = Address.buildAddress(0x0200)
        val absolute = Argument.AbsoluteX(address, x, bus)
        absolute.write(0xFA.toByte())
        assertEquals(bus.read(address + x.value), absolute.read())
        assertEquals("$0200,X", absolute.toString())
    }

    @Test
    fun `AbsoluteY Test`() {
        y.value = 0x5
        val address = Address.buildAddress(0x0300)
        val absolute = Argument.AbsoluteY(address, y, bus)
        absolute.write(0xFA.toByte())
        assertEquals(bus.read(address + y.value), absolute.read())
        assertEquals("$0300,Y", absolute.toString())
    }

    @Test
    fun `Indirect Test`() {
        val indirectAddress = Address.buildAddress(0x0321)
        bus.write(indirectAddress, 0x01)
        bus.write(indirectAddress + 1, 0x02)
        val indirect = Argument.Indirect(indirectAddress, bus)
        assertEquals(0x0201, indirect.address.value)
        assertEquals("(\$0321)", indirect.toString())
    }

    @Test
    fun `IndexedIndirect Test`() {
        x.value = 0xA0.toByte()
        val base: Byte = 0x05
        val address = Address.buildAddress(0, base.toInt())
        bus.write(address + x.value, 0x02)
        bus.write(address + x.value + 1, 0x03)
        val indirectAddress = Address.buildAddress(0x0302)
        val indexedIndirect = Argument.IndexedIndirect(base, x, bus)
        indexedIndirect.write(0xCA.toByte())
        assertEquals(bus.read(indirectAddress), indexedIndirect.read())
        assertEquals("(\$05,X)", indexedIndirect.toString())
    }

    @Test
    fun `IndirectIndexed Test`() {
        y.value = 0xB0.toByte()
        val base: Byte = 0x12
        val address = Address.buildAddress(0, base.toInt())
        bus.write(address, 0x0A)
        bus.write(address + 1, 0x04)
        val indirectAddress = Address.buildAddress(0x04BA)
        val indexedIndirect = Argument.IndirectIndexed(base, y, bus)
        indexedIndirect.write(0x2B.toByte())
        assertEquals(bus.read(indirectAddress), indexedIndirect.read())
        assertEquals("(\$12),Y", indexedIndirect.toString())
    }
}