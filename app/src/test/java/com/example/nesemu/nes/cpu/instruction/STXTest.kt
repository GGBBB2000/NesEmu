package com.example.nesemu.nes.cpu.instruction

import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.cpu.Register
import com.example.nesemu.nes.util.Address
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test

class STXTest {
   @Test
   fun stxTest() {
      val x = Register.X(0xAB.toByte())
      val bus = Bus(mockk(), mockk())
      val dest = Address.buildAddress(0x1234)
      STX(x, dest, bus).exec()
      assertEquals(bus.read(dest), x.value)
   }
}