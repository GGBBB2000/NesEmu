package com.example.nesemu.nes.cpu

import org.junit.Assert.*
import org.junit.Test

class RegisterTest {
    @Test
    fun setTest() {
        val status = Register.P()

        var data: Byte = 0b0111_0101
        status.setFlag(data)
        assertEquals(data, status.toByte())
        data = 0b1010_1010.toByte()
        status.setFlag(data)
        assertEquals(data, status.toByte())
        data = 0b0
        status.setFlag(data)
        assertEquals(0b0010_0000.toByte(), status.toByte())
    }
}