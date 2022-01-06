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

    fun reset() {
        a.value = 0
        x.value = 0
        y.value = 0
        sp.setAddress(0xFF.toByte())
        p.setFlag(0)
        val pcLow = read(Address.buildAddress(0xFFFC)).toInt()
        val pcHigh = read(Address.buildAddress(0xFFFD)).toInt()
        pc.address = Address.Companion.buildAddress(pcHigh, pcLow)
    }

    override fun read(address: Address): Byte = bus.read(address)

    override fun write(address: Address, data: Byte) = bus.write(address, data)

    private fun getAbsoluteAddress() : Address {
        val lower = read(pc.address++)
        val upper = read(pc.address++)
        return Address.buildAddress(upper.toInt(), lower.toInt())
    }

    private fun getImmediateValue() = read(pc.address++)

    private fun getXIndexedAbsoluteAddress() : Address = getAbsoluteAddress() + x.value

    private fun getYIndexedAbsoluteAddress() : Address = getAbsoluteAddress() + y.value

    private fun getZeroPageAddress(data: Byte) : Address = Address.buildAddress(0, data.toInt())

    private fun getZeroPageXIndexedAddress() : Address = Address.buildAddress(0, getImmediateValue() + x.value)

    private fun getZeroPageYIndexedAddress() : Address = Address.buildAddress(0, getImmediateValue() + y.value)

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
            0xD0 -> InstructionInfo(0xD0.toByte(), BNE(pc, p, getImmediateValue()), 2) // +1 or 2 ブランチで+1 ページクロスで+2
            //BVC
            //BVS
            //BPL
            //BMI
            //BIT
            //JMP
            0x4C -> InstructionInfo(0x4C, JMP(getAbsoluteAddress(), pc), 3)
            //JSR
            0x20 -> InstructionInfo(0x20, JSR(pc, sp, getAbsoluteAddress(), bus), 6)
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
            0xCA -> InstructionInfo(0xCA.toByte(), DEX(x, p), 2)
            //INY
            //DEY
            0x88 -> InstructionInfo(0x88.toByte(), DEY(y, p), 2)
            //CLC
            //SEC
            0x78 -> InstructionInfo(0x78, SEI(p), 2)
            //CLD
            //SED
            //CLV
            //LDA
            0xA9 -> InstructionInfo(0xA9.toByte(), LDA(getImmediateValue(), a, p), 2)
            0xAD -> InstructionInfo(0xAD.toByte(), LDA(read(getAbsoluteAddress()), a, p), 4)
            0xBD -> InstructionInfo(0xBD.toByte(), LDA(read(getXIndexedAbsoluteAddress()), a, p), 4) // ページクロスで+1
            0xB9 -> InstructionInfo(0xBD.toByte(), LDA(read(getYIndexedAbsoluteAddress()), a, p), 4) // ページクロスで+1
            //LDX
            0xA2 -> InstructionInfo(0xA2.toByte(), LDX(getImmediateValue(), x, p), 2)
            //LDY
            0xA0 -> InstructionInfo(0xA0.toByte(), LDY(getImmediateValue(), y, p), 2)
            //STA
            0x8D -> InstructionInfo(0x8D.toByte(), STA(a, getAbsoluteAddress(), bus), 4)
            0x95 -> InstructionInfo(0x95.toByte(), STA(a, getZeroPageXIndexedAddress(), bus), 4)
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