package com.example.nesemu.nes

import com.example.nesemu.nes.util.Address
import com.example.nesemu.nes.util.IODevice

class Rom(private val rom: ByteArray) : IODevice {

    override fun read(address: Address): Byte = this.rom[address.value]

    override fun write(address: Address, data: Byte) {
        error("read-only")
    }
}