package com.example.nesemu.nes

import com.example.nesemu.nes.cartridge.Cartridge
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths

class CartridgeTest {
    @Test
    fun fileReadTest() {
        val file: ByteArray = Files.readAllBytes(Paths.get("./src/main/assets/rom/helloworld/sample1/sample1.nes"))
        println(file.size)
        val cartridge = Cartridge.getCartridge(file)
    }
}