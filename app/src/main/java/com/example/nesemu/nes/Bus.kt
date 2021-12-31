package com.example.nesemu.nes

import com.example.nesemu.nes.util.Address
import com.example.nesemu.nes.util.IODevice

class Bus(private val cartridge: Cartridge) : IODevice {
    private val ram = Ram(0x800)

    override fun read(address: Address): Byte {
        return when(address.value) {
            in 0x0000 until 0x800 -> ram.read(address)
            in 0x800 until 0x2000 -> { // RAM ミラー 1~3
                address -= 0x800
                address %= 0x800
                ram.read(address)
            }
            // in 0x2000 until 0x2007 ppu io
            // in 0x2000 until 0x2007 ppu io
            // in 0x4000 until 0x4020 apu pad
            // in 0x4020 until 0x6000 ext rom
            // in 0x6000 until 0x8000 ext ram
            in 0x8000 until 0xC000 -> {
                address -= 0x8000
                cartridge.read(address)
            }
            in 0xC000..0xFFFF -> {
                address -= 0x8000
                cartridge.read(address)
            }
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
            in 0x2000 until 0x2008 -> {}
            // in 0x2008 until 0x4000 ppu io mirror * 1023
            // in 0x4000 until 0x4020 apu pad
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
}