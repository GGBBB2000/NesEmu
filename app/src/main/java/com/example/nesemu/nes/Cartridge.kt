package com.example.nesemu.nes

import com.example.nesemu.nes.util.Address
import com.example.nesemu.nes.util.IODevice

class Cartridge private constructor(private val header: INes, private val prgRom: Rom, private val chrRom: Rom) {
    companion object {
        fun getCartridge(cartridgeData: ByteArray): Cartridge {
            val headerSize = 16;
            val headerData = cartridgeData.slice(0 until headerSize);
            val nesString = String(headerData.slice(0 until 3).toByteArray())
            if ((nesString != "NES") and (headerData[3] != 0x1A.toByte())) {
                error("No iNES header found.")
            }
            val prgRomSize = headerData[4] * 16 * 1024
            val charRomSize = headerData[5] * 8 * 1024
            val prgRom = cartridgeData.slice(headerSize until (headerSize + prgRomSize)).toByteArray()
            val chrRom = cartridgeData.slice((headerSize + prgRomSize) until (headerSize + prgRomSize + charRomSize)).toByteArray()
            val iNes = INes(nesString, headerData[4], headerData[5], headerData[6], headerData[7], headerData[8], headerData[9], headerData[10])
            return Cartridge(iNes, Rom(prgRom), Rom(chrRom))
        }
    }

    fun readPrgRom(address: Address): Byte = this.prgRom.read(address)

    fun readChrRom(address: Address): Byte = this.chrRom.read(address)

    data class INes(val nes: String,
                    val prgSize: Byte,
                    val chrSize: Byte,
                    val flag6: Byte,
                    val flag7: Byte,
                    val flag8: Byte,
                    val flag9: Byte,
                    val flag10: Byte) {
        override fun toString(): String {
            return """
                $nes
                $prgSize * 16KiB
                $chrSize * 8KiB
                flag6:  0b${flag6.toString(2)}
                flag7:  0b${flag7.toString(2)}
                flag8:  0b${flag8.toString(2)}
                flag9:  0b${flag9.toString(2)}
                flag10: 0b${flag10.toString(2)}
            """.trimIndent()
        }
    }
    /*
    * iNES header
    * 0-3: Constant $4E $45 $53 $1A ("NES" followed by MS-DOS end-of-file)
    * 4: Size of PRG ROM in 16 KB units
    * 5: Size of CHR ROM in 8 KB units (Value 0 means the board uses CHR RAM)
    * 6: Flags 6 - Mapper, mirroring, battery, trainer
    *   76543210
        ||||||||
        |||||||+- Mirroring: 0: horizontal (vertical arrangement) (CIRAM A10 = PPU A11)
        |||||||              1: vertical (horizontal arrangement) (CIRAM A10 = PPU A10)
        ||||||+-- 1: Cartridge contains battery-backed PRG RAM ($6000-7FFF) or other persistent memory
        |||||+--- 1: 512-byte trainer at $7000-$71FF (stored before PRG data)
        ||||+---- 1: Ignore mirroring control or above mirroring bit; instead provide four-screen VRAM
        ++++----- Lower nybble of mapper number
    * 7: Flags 7 - Mapper, VS/Playchoice, NES 2.0
    *   76543210
        ||||||||
        |||||||+- VS Unisystem
        ||||||+-- PlayChoice-10 (8KB of Hint Screen data stored after CHR data)
        ||||++--- If equal to 2, flags 8-15 are in NES 2.0 format
        ++++----- Upper nybble of mapper number
    * 8: Flags 8 - PRG-RAM size (rarely used extension)
    * 9: Flags 9 - TV system (rarely used extension)
    * 10: Flags 10 - TV system, PRG-RAM presence (unofficial, rarely used extension)
    * 11-15: Unused padding (should be filled with zero, but some rippers put their name across bytes 7-15)
    */
}