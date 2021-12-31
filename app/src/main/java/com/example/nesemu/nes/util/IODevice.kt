package com.example.nesemu.nes.util

import com.example.nesemu.nes.util.Address

interface IODevice {
    fun read(address: Address) :Byte

    fun write(address: Address, data: Byte)
}