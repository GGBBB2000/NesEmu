package com.example.nesemu.nes

import com.example.nesemu.nes.cartridge.Cartridge
import com.example.nesemu.nes.util.Address
import com.example.nesemu.nes.util.IODevice

class Bus(private val cartridge: Cartridge, val ppu: Ppu, val joyPad: JoyPad) : IODevice {
    private val ram = Ram(0x800)
    var dmaLock = false

    override fun read(address: Address): Byte {
        return when(address.value) {
            in 0x0000 until 0x2000 -> ram.read(address % 0x800) // RAMとそのミラー1~3
            in 0x2000 until 0x2007 -> ppu.read(address)
            // in 0x4000 until 0x4020 apu pad
            0x4016 -> joyPad.read(address)
            // in 0x4020 until 0x6000 ext rom
            // in 0x6000 until 0x8000 ext ram
            in 0x8000 .. 0xFFFF -> cartridge.readPrgRom(address % 0x8000)
            else -> error("read ${address.value} out of bounds")
        }
    }

    override fun write(address: Address, data: Byte) {
        when(address.value) {
            in 0x0000 until 0x800 -> ram.write(address, data)
            in 0x800 until 0x2000 -> { // RAM ミラー 1~3
                address -= 0x800
                address %= 0x800
                ram.write(address, data)
            }
            in 0x2000 until 0x2008 -> ppu.write(address, data)
            // in 0x2008 until 0x4000 ppu io mirror * 1023
            in 0x4000 .. 0x4013, 0x4015, 0x4017 -> {} // apu JoyPad2
            0x4014 -> transferSpriteToPpu(data) // DMA
            0x4016 -> joyPad.write(address, data)
            // in 0x4020 until 0x6000 ext rom
            // in 0x6000 until 0x8000 ext ram
            in 0x8000 until 0xC000 -> {
                address -= 0x8000
                //cartridge.read(address)
            }
            in 0xC000..0xFFFF -> {
                address -= 0x8000
                //cartridge.read(address)
            }
            else -> error("write ${address.value} out of bounds")
        }
    }

    private fun transferSpriteToPpu(addressHigh: Byte) {
        dmaLock = true
        var address = Address.buildAddress(addressHigh.toInt(), 0)
        for (index in 0x0 until 0x100) {
            ppu.writeSpriteData(index, read(address++))
        }
    }
}