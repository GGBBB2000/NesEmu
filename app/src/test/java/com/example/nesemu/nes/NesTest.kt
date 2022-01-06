package com.example.nesemu.nes

import com.example.nesemu.nes.cartridge.Cartridge
import org.junit.Test

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