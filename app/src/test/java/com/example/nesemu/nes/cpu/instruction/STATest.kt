package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.cpu.Register
import com.example.nesemu.nes.util.Address
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

internal class STATest {

    private lateinit var bus: Bus

    private lateinit var register: Register.A

    @Before
    fun setUp() {
        bus = Bus(mockk(), mockk())
        register = Register.A()
        register.value = 1
    }

    @Test
    fun storeATo0x0000AndMirror() {
        val dest = Address.buildAddress(0x0)
        var inst = STA(register, dest, bus)
        inst.exec()
        assert(bus.read(dest).toInt() == 1)
        assert(bus.read(Address.buildAddress(0x800)).toInt() == 1)
        assert(bus.read(Address.buildAddress(0x800 * 2)).toInt() == 1)
        assert(bus.read(Address.buildAddress(0x800 * 3)).toInt() == 1)

        register.value = 0xFF.toByte()
        inst = STA(register, dest, bus)
        inst.exec()

        assert(bus.read(dest).toInt() == -1)
        assert(bus.read(Address.buildAddress(0x800)).toInt() == -1)
        assert(bus.read(Address.buildAddress(0x800 * 2)).toInt() == -1)
        assert(bus.read(Address.buildAddress(0x800 * 3)).toInt() == -1)
    }

    @Test
    fun storeATo0x07FFAndMirror() {
        val dest = Address.buildAddress(0x07FF)
        var inst = STA(register, dest, bus)
        inst.exec()
        assert(bus.read(dest).toInt() == 1)
        assert(bus.read(Address.buildAddress(0x800 + 0x7FF)).toInt() == 1)
        assert(bus.read(Address.buildAddress(0x800 * 2 + 0x7FF)).toInt() == 1)
        assert(bus.read(Address.buildAddress(0x800 * 3 + 0x7FF)).toInt() == 1)

        register.value = 0xFF.toByte()
        inst = STA(register, dest, bus)
        inst.exec()

        assert(bus.read(dest).toInt() == -1)
        assert(bus.read(Address.buildAddress(0x800 + 0x7FF)).toInt() == -1)
        assert(bus.read(Address.buildAddress(0x800 * 2 + 0x7FF)).toInt() == -1)
        assert(bus.read(Address.buildAddress(0x800 * 3 + 0x7FF)).toInt() == -1)
    }

    @Test
    fun storeATo0x0001AndMirror() {
        register.value = 0x1
        val dest = Address.buildAddress(0x0001)
        var inst = STA(register, dest, bus)
        inst.exec()
        assert(bus.read(dest).toInt() == 1)
        assert(bus.read(Address.buildAddress(0x800 + 1)).toInt() == 1)
        assert(bus.read(Address.buildAddress(0x800 * 2 + 1)).toInt() == 1)
        assert(bus.read(Address.buildAddress(0x800 * 3 + 1)).toInt() == 1)

        register.value = 0xFF.toByte()
        inst = STA(register, dest, bus)
        inst.exec()

        assert(bus.read(dest).toInt() == -1)
        assert(bus.read(Address.buildAddress(0x800 + 1)).toInt() == -1)
        assert(bus.read(Address.buildAddress(0x800 * 2 + 1)).toInt() == -1)
        assert(bus.read(Address.buildAddress(0x800 * 3 + 1)).toInt() == -1)
    }
}