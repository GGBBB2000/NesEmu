package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register
import com.example.nesemu.nes.util.Address
import org.junit.Assert.assertEquals

import org.junit.Test

class BCCTest {
    private val pc = Register.PC(Address.buildAddress(0x81))
    private val p = Register.P()
    // オフセットは負の数なので分岐先は0x1
    private val offset = 0x80.toByte()

    @Test
    fun `BCC C = true`() {
        // フラグがセットされていれば分岐しない
        p.carry = true
        BCC(pc, p, offset).exec()
        assertEquals(0x81, pc.address.value)
    }

    @Test
    fun `BCC C = false`() {
        // フラグがクリアされていれば分岐
        p.carry = false
        BCC(pc, p, offset).exec()
        assertEquals(0x1, pc.address.value)
    }
}