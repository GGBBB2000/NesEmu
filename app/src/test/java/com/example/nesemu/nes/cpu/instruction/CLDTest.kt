package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register
import org.junit.Assert.*
import org.junit.Test

class CLDTest {
    private val p = Register.P()

    /*
        flag N,V,1,B,D,I,Z,C
        10101010->10100010
        10100010->10100010
     */
    @Test
    fun clearFlagTest() {
        p.setFlag(0b10101010.toByte())
        CLD(p).exec()
        assertEquals(0b10100010.toByte(), p.toByte())
        CLD(p).exec()
        assertEquals(0b10100010.toByte(), p.toByte())
    }
}