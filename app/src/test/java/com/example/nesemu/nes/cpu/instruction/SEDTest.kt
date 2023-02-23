package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register
import org.junit.Assert.*
import org.junit.Test

class SEDTest {
    private val p = Register.P()

    /*
        flag N,V,1,B,D,I,Z,C
        10100010->10101010
        10101010->10101010
     */
    @Test
    fun clearFlagTest() {
        p.setFlag(0b10100010.toByte())
        SED(p).exec()
        assertEquals(0b10101010.toByte(), p.toByte())
        SED(p).exec()
        assertEquals(0b10101010.toByte(), p.toByte())
    }
}