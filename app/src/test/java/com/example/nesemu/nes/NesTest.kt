package com.example.nesemu.nes

import org.junit.Test

import org.junit.Assert.*
import java.nio.file.Files
import java.nio.file.Paths

class NesTest {

    @Test
    fun run() {
        val file: ByteArray = Files.readAllBytes(Paths.get("./src/main/assets/rom/helloworld/sample1/sample1.nes"))
        val cartridge = Cartridge.getCartridge(file)
        val nes = Nes(cartridge)
        while (true) {
            nes.run()
        }
    }
}