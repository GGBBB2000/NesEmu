package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register
import com.example.nesemu.nes.util.Address
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class BNETest {
    private val pc = Register.PC(Address.buildAddress(0x81))
    private val p = Register.P()
    // オフセットは負の数なので分岐先は0x1
    private val offset = 0x80.toByte()

    @Before
    fun setup() {
        pc.address.value = 0x81
    }

    @Test
    fun `BNE Z = true`() {
        // フラグがセットされていれば分岐しない
        p.zero= true
        BNE(pc, p, offset).exec()
        assertEquals(0x81, pc.address.value)
    }

    @Test
    fun `BNE C = false`() {
        // フラグがクリアされていれば分岐
        p.zero = false
        BNE(pc, p, offset).exec()
        assertEquals(0x1, pc.address.value)
    }
}