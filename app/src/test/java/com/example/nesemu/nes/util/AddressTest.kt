package com.example.nesemu.nes.util

import org.junit.Assert.*
import org.junit.Test

class AddressTest {
    @Test
    fun addressBuildTest() {
        assertEquals(0x0102, Address.buildAddress(0x01, 0x02).value)
        assertEquals(0x01FF, Address.buildAddress(0x01, 0xFF).value)
        assertEquals(0xF802, Address.buildAddress(0xF8, 0x02).value)
        assertEquals(0xF8E1, Address.buildAddress(0xF8, 0xE1).value)
    }

    @Test
    fun plusTest() {
        for (i in 0x0..0xFF) {
            val address = Address.buildAddress(0x10, 0x0)
            address += i.toByte()
            println(address)
        }
    }
}