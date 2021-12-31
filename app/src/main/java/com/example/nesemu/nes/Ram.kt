package com.example.nesemu.nes

import com.example.nesemu.nes.util.Address
import com.example.nesemu.nes.util.IODevice

class Ram(size: Int) : IODevice {
    private val ram = ByteArray(size)

    override fun read(address: Address): Byte = ram[address.value]

    override fun write(address: Address, data: Byte) {
        this.ram[address.value] = data
    }
}