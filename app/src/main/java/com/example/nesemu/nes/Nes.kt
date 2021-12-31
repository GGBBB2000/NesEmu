package com.example.nesemu.nes

import com.example.nesemu.nes.cpu.Cpu

class Nes(private val cartridge: Cartridge) {
    private val bus = Bus(cartridge)
    private val cpu = Cpu(bus)

    fun run() {
        cpu.run()
    }
}