package com.example.nesemu

import com.example.nesemu.nes.Nes
import com.example.nesemu.nes.Ppu
import java.util.*

abstract class MainModule {
    internal lateinit var timer: Timer
    internal lateinit var nes: Nes
    internal lateinit var screen: Ppu.Screen
}

class DefaultMainModule: MainModule {
    constructor() {
        timer = Timer()
        nes = Nes()
        screen = nes.getScreen()
    }
}

