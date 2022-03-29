package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register
import com.example.nesemu.nes.util.Address
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TSXTest {
    private val x = Register.X()
    private val sp = Register.SP(Address.Companion.buildAddress(0xFF))
    private val p = Register.P()

    @Before
    fun setup() {
        x.value = 0
        p.setFlag(0)
    }

    /*
        flag N,V,1,B,D,I,Z,C = 00100000
     */
    @Test
    fun `TSX N = false Z = false`() {
        sp.setAddress(0x1)
        TSX(x, sp, p).exec()
        assertEquals(0b00100000.toByte(), p.toByte())
    }

    /*
        flag N,V,1,B,D,I,Z,C = 00100010
     */
    @Test
    fun `TSX N = false Z = true`() {
        sp.setAddress(0x0)
        TSX(x, sp, p).exec()
        assertEquals(0b00100010.toByte(), p.toByte())
    }

    /*
        flag N,V,1,B,D,I,Z,C = 10100000
     */
    @Test
    fun `TSX N = true Z = false`() {
        sp.setAddress(0xFF.toByte())
        TSX(x, sp, p).exec()
        assertEquals(0b10100000.toByte(), p.toByte())
    }
}