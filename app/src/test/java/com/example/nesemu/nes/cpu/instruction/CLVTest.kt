package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register
import org.junit.Assert.*
import org.junit.Test

class CLVTest {
    private val p = Register.P()

    /*
        flag N,V,1,B,D,I,Z,C
        01101010->00101010
        00101010->00101010
     */
    @Test
    fun clearFlagTest() {
        p.setFlag(0b01101010.toByte())
        CLV(p).exec()
        assertEquals(0b00101010.toByte(), p.toByte())
        CLV(p).exec()
        assertEquals(0b00101010.toByte(), p.toByte())
    }
}