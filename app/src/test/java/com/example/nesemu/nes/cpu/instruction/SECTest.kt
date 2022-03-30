package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register
import org.junit.Assert.*
import org.junit.Test

class SECTest {
    private val p = Register.P()
    /*
        flag N,V,1,B,D,I,Z,C
        10101010->10101011
        10101011->10101011
     */
    @Test
    fun setCFlagTest() {
        p.setFlag(0b10101010.toByte())
        SEC(p).exec()
        assertEquals(0b10101011.toByte(), p.toByte())
        SEC(p).exec()
        assertEquals(0b10101011.toByte(), p.toByte())
    }

}