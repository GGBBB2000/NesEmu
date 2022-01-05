package com.example.nesemu.nes

import com.example.nesemu.nes.cpu.Cpu

class Nes(cartridge: Cartridge) {
    private val ppu = Ppu(cartridge)
    private val bus = Bus(cartridge, ppu)
    private val cpu = Cpu(bus)

    fun run() {
        var totalCycle = 0
        while (totalCycle < 341 * 262) {
            val cycle = cpu.run()
            ppu.run(cycle * 3)
            totalCycle += cycle * 3
        }
    }

    fun getScreen(): Ppu.Screen = ppu.screen
}