package com.example.nesemu.nes.cartridge

import com.example.nesemu.nes.Rom
import com.example.nesemu.nes.util.Address

class Mapper0(private val header: INes, private val prgRom: Rom, private val chrRom: Rom) : Cartridge() {
    /*
        ROMのサイズは0x4000 or 0x8000なのでサイズが0x4000のときは
        0x8000~0xBFFFを0xC000~0xFFFFにミラーする
     */
    override fun readPrgRom(address: Address): Byte {
        val romSize = header.prgSize * 16 * 1024
        return if (romSize == 0x4000) {
           prgRom.read(address % 0x4000)
        } else {
            prgRom.read(address)
        }
    }

    override fun readChrRom(address: Address): Byte = chrRom.read(address)
}