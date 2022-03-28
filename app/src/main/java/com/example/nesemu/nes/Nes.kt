package com.example.nesemu.nes

import com.example.nesemu.nes.cartridge.Cartridge
import com.example.nesemu.nes.cpu.Cpu

class Nes {
    private var cartridge: Cartridge? = null
    private val nmi = NMI()
    private val ppu = Ppu(nmi)
    val joyPad = JoyPad()
    private val bus = Bus(ppu, joyPad)
    private val cpu = Cpu(bus, nmi)

    fun reset() {
        cpu.reset()
    }

    fun insertCartridge(cartridge: Cartridge) {
        this.cartridge = cartridge
        ppu.insertCartridge(cartridge)
        bus.insertCartridge(cartridge)
    }

    fun ejectCartridge() {
        cartridge = null
        ppu.ejectCartridge()
        bus.ejectCartridge()
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