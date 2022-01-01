package com.example.nesemu.nes

import com.example.nesemu.nes.cpu.Cpu

class Nes(cartridge: Cartridge) {
    private val ppu = Ppu(cartridge)
    private val bus = Bus(cartridge, ppu)
    private val cpu = Cpu(bus)

    fun run() {
        cpu.run()
    }
}