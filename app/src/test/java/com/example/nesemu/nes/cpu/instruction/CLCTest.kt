package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register
import org.junit.Assert.*
import org.junit.Test

class CLCTest {
    private val p = Register.P()

    /*
        flag N,V,1,B,D,I,Z,C
        10101011->10101010
        10101010->10101010
     */
    @Test
    fun clearFlagTest() {
        p.setFlag(0b10101011.toByte())
        CLC(p).exec()
        assertEquals(0b10101010.toByte(), p.toByte())
        CLC(p).exec()
        assertEquals(0b10101010.toByte(), p.toByte())
    }
}