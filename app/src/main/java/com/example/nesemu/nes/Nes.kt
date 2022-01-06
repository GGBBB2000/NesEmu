package com.example.nesemu.nes

import com.example.nesemu.nes.cartridge.Cartridge
import com.example.nesemu.nes.cpu.Cpu

class Nes(cartridge: Cartridge) {
    private val nmi = NMI()
    private val ppu = Ppu(cartridge, nmi)
    private val bus = Bus(cartridge, ppu)
    private val cpu = Cpu(bus, nmi)

    fun reset() {
        cpu.reset()
    }

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