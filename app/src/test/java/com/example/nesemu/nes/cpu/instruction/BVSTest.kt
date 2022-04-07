package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.cpu.Register
import com.example.nesemu.nes.util.Address
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class BVSTest {
    private val pc = Register.PC(Address.buildAddress(0x81))
    private val p = Register.P()
    // オフセットは負の数なので分岐先は0x1
    private val offset = 0x80.toByte()

    @Before
    fun setup() {
        pc.address.value = 0x81
    }

    @Test
    fun `BVS V = true`() {
        // フラグがセットされていれば分岐
        p.overflow = true
        BVS(pc, p, offset).exec()
        assertEquals(0x1, pc.address.value)
    }

    @Test
    fun `BVS V = false`() {
        // フラグがクリアされていれば分岐しない
        p.overflow = false
        BVS(pc, p, offset).exec()
        assertEquals(0x81, pc.address.value)
    }
}