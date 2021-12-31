package com.example.nesemu.nes.cpu

import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.cpu.instruction.*
import com.example.nesemu.nes.util.IODevice
import com.example.nesemu.nes.util.Address

class Cpu(val bus: Bus) : IODevice {
    val a = Register.A()
    val x = Register.X()
    val y = Register.Y()
    val pc = Register.PC(Address.buildAddress(0x80, 0x00))
    val sp = Register.SP(Address.buildAddress(0x01, 0xFF))
    val p = Register.P()

    override fun read(address: Address): Byte = bus.read(address)

    override fun write(address: Address, data: Byte) = bus.write(address, data)

    private fun getAbsoluteAddress() : Address {
        val lower = read(pc.address++)
        val upper = read(pc.address++)
        return Address.buildAddress(upper.toInt(), lower.toInt())
    }

    private fun getIndexedAbsoluteAddress(x: Register.X) : Address = getAbsoluteAddress() + x.value

    private fun getIndexedAbsoluteAddress(y: Register.Y) : Address = getAbsoluteAddress() + y.value

    fun run() : Int {
        val instInfo = fetch()
        instInfo.inst.exec()
        return instInfo.cycle
    }

    private fun fetch() : InstructionInfo {
        val opcode = read(pc.address++).toInt() and 0xFF
        return when (opcode) {
            // ADC
            //SBC
            //AND
            //ORA
            //EOR
            //ASL
            //LSR
            //ROL
            //ROR
            //BCC
            //BCS
            //BEQ
            //BNE
            //BVC
            //BVS
            //BPL
            //BMI
            //BIT
            //JMP
            //JSR
            //RTS
            //BRK
            //RTI
            //CMP
            //CPX
            //CPY
            //INC
            //DEC
            //INX
            0xE8 -> InstructionInfo(0xE8.toByte(), INX(x, p),2)
            //DEX
            //INY
            //DEY
            //CLC
            //SEC
            0x78 -> InstructionInfo(0x78, SEI(p), 2)
            //CLD
            //SED
            //CLV
            //LDA
            0xA9 -> InstructionInfo(0xA9.toByte(), LDA(read(pc.address++), a, p), 2)
            0xBD -> InstructionInfo(0xBD.toByte(), LDA(read(getIndexedAbsoluteAddress(x)), a, p), 4) // ページクロスで+1
            //LDX
            0xA2 -> InstructionInfo(0xA2.toByte(), LDX(read(pc.address++), x, p), 2)
            //LDY
            0xA0 -> InstructionInfo(0xA0.toByte(), LDY(read(pc.address++), y, p), 2)
            //STA
            0x8D -> InstructionInfo(0x8D.toByte(), STA(a, getAbsoluteAddress(), bus), 4)
            //STX
            //STY
            //TAX
            //TXA
            //TAY
            //TYA
            0x9A -> InstructionInfo(0x9A.toByte(), TXS(x, sp), 2)
            //TSX
            //PHA
            //PLA
            //PHP
            //PLP

            else -> error("[${opcode.toString(16)}] not implemented")
        }
    }
}