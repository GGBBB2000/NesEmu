package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register
import com.example.nesemu.nes.util.Address
import org.junit.Assert.*
import org.junit.Test

class BCSTest {
    private val pc = Register.PC(Address.buildAddress(0x81))
    private val p = Register.P()
    // オフセットは負の数なので分岐先は0x1
    private val offset = 0x80.toByte()

    @Test
    fun `BCS C = true`() {
        // フラグがセットされていれば分岐
        p.carry = true
        BCS(pc, p, offset).exec()
        assertEquals(0x1, pc.address.value)
    }

    @Test
    fun `BCS C = false`() {
        // フラグがクリアされていれば分岐しない
        p.carry = false
        BCS(pc, p, offset).exec()
        assertEquals(0x81, pc.address.value)
    }
}