package com.example.nesemu.nes.cpu

import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.NMI
import com.example.nesemu.nes.cpu.instruction.*
import com.example.nesemu.nes.util.IODevice
import com.example.nesemu.nes.util.Address

class Cpu(val bus: Bus, val nmi: NMI) : IODevice {
    lateinit var a : Register.A
    lateinit var x : Register.X
    lateinit var y : Register.Y
    lateinit var pc: Register.PC
    lateinit var sp: Register.SP
    lateinit var p: Register.P

    init {
        reset()
    }

    fun reset() {
        a = Register.A()
        x = Register.X()
        y = Register.Y()
        sp = Register.SP(Address.buildAddress(0x01FF))
        p = Register.P()
        val pcLow = read(Address.buildAddress(0xFFFC)).toInt()
        val pcHigh = read(Address.buildAddress(0xFFFD)).toInt()
        pc = Register.PC(Address.buildAddress(pcHigh, pcLow))
        nmi.hasInterrupt = false
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

    private fun getZeroPageAddress() : Address = Address.buildAddress(0, getImmediateValue().toInt())

    private fun getZeroPageXIndexedAddress() : Address = Address.buildAddress(0, getImmediateValue() + x.value)

    private fun getZeroPageYIndexedAddress() : Address = Address.buildAddress(0, getImmediateValue() + y.value)

    private fun getIndirectYAddress() : Address = getZeroPageAddress() + y.value

    fun run() : Int {
        if (nmi.hasInterrupt) {
            p.breakFlag = false
            handleInterrupt(Address.buildAddress(0xFFFB), Address.buildAddress(0xFFFA))
            nmi.hasInterrupt = false
            return 8
        }
        val instInfo = fetch()
        instInfo.inst.exec()
        return instInfo.cycle
    }

    private fun handleInterrupt(addressHigh: Address, addressLow : Address) {
        write(sp.address--, ((pc.address.value and 0xFF00) ushr 8).toByte())
        write(sp.address--, ((pc.address.value and 0xFF).toByte()))
        write(sp.address--, p.toByte())
        p.interrupt = true
        val pcLow = read(addressLow).toInt()
        val pcHigh = read(addressHigh).toInt()
        pc.address = Address.buildAddress(pcHigh, pcLow)
    }

    private fun fetch() : InstructionInfo {
        val opcode = read(pc.address++).toInt() and 0xFF
        return when (opcode) {
            // ADC
            //SBC
            //AND
            0x25 -> InstructionInfo(0x25.toByte(), AND(read(getZeroPageAddress()), a, p), 3)
            //ORA
            //EOR
            0x45 -> InstructionInfo(0x45.toByte(), EOR(read(getZeroPageAddress()), a, p), 3)
            //ASL
            //LSR
            0x4A -> InstructionInfo(0x4A, LSR(a, null, p, bus), 2)
            //ROL
            0x26 -> InstructionInfo(0x26, ROL(a, getZeroPageAddress(), p, bus), 5)
            //ROR
            //BCC
            //BCS
            //BEQ
            0xF0 -> InstructionInfo(0xF0.toByte(), BEQ(pc, p, getImmediateValue()), 2)
            //BNE
            0xD0 -> InstructionInfo(0xD0.toByte(), BNE(pc, p, getImmediateValue()), 2) // +1 or 2 ブランチで+1 ページクロスで+2
            //BVC
            //BVS
            //BPL
            0x10 -> InstructionInfo(0x10, BPL(pc, p, getImmediateValue()), 2) // +1 or 2 ブランチで+1 ページクロスで+2
            //BMI
            //BIT
            //JMP
            0x4C -> InstructionInfo(0x4C, JMP(getAbsoluteAddress(), pc), 3)
            //JSR
            0x20 -> InstructionInfo(0x20, JSR(pc, sp, getAbsoluteAddress(), bus), 6)
            //RTS
            0x60 -> InstructionInfo(0x60, RTS(pc, sp, bus), 6)
            //BRK
            //RTI
            0x40 -> InstructionInfo(0x40.toByte(), RTI(pc, sp, p, bus), 6)
            //CMP
            0xC9 -> InstructionInfo(0xC9.toByte(), CMP(getImmediateValue(), a, p), 2)
            0xC5 -> InstructionInfo(0xC9.toByte(), CMP(read(getZeroPageAddress()), a, p), 3)
            //CPX
            0xE0 -> InstructionInfo(0xE0.toByte(), CPX(getImmediateValue(), x, p), 2)
            //CPY
            //INC
            0xE6 -> InstructionInfo(0xE6.toByte(), INC(p, getZeroPageAddress(), bus), 5)
            //DEC
            0xC6 -> InstructionInfo(0xC6.toByte(), DEC(p, getZeroPageAddress(), bus), 5)
            //INX
            0xE8 -> InstructionInfo(0xE8.toByte(), INX(x, p),2)
            //DEX
            0xCA -> InstructionInfo(0xCA.toByte(), DEX(x, p), 2)
            //INY
            0xC8 -> InstructionInfo(0xC8.toByte(), INY(y, p),2)
            //DEY
            0x88 -> InstructionInfo(0x88.toByte(), DEY(y, p), 2)
            //CLC
            //SEC
            0x78 -> InstructionInfo(0x78, SEI(p), 2)
            //CLD
            0xd8 -> InstructionInfo(0xd8.toByte(), CLD(p), 2)
            //SED
            //CLV
            //LDA
            0xA9 -> InstructionInfo(0xA9.toByte(), LDA(getImmediateValue(), a, p), 2)
            0xA5 -> InstructionInfo(0xA9.toByte(), LDA(read(getZeroPageAddress()), a, p), 3)
            0xAD -> InstructionInfo(0xAD.toByte(), LDA(read(getAbsoluteAddress()), a, p), 4)
            0xBD -> InstructionInfo(0xBD.toByte(), LDA(read(getXIndexedAbsoluteAddress()), a, p), 4) // ページクロスで+1
            0xB9 -> InstructionInfo(0xBD.toByte(), LDA(read(getYIndexedAbsoluteAddress()), a, p), 4) // ページクロスで+1
            0xB1 -> InstructionInfo(0xB1.toByte(), LDA(read(getIndirectYAddress()), a, p), 5) // ページクロスで+1
            //LDX
            0xA2 -> InstructionInfo(0xA2.toByte(), LDX(getImmediateValue(), x, p), 2)
            //LDY
            0xA0 -> InstructionInfo(0xA0.toByte(), LDY(getImmediateValue(), y, p), 2)
            0xA4 -> InstructionInfo(0xA4.toByte(), LDY(read(getZeroPageAddress()), y, p), 3)
            //STA
            0x85 -> InstructionInfo(0x85.toByte(), STA(a, getZeroPageAddress(), bus), 3)
            0x95 -> InstructionInfo(0x95.toByte(), STA(a, getZeroPageXIndexedAddress(), bus), 4)
            0x8D -> InstructionInfo(0x8D.toByte(), STA(a, getAbsoluteAddress(), bus), 4)
            0x91 -> InstructionInfo(0x91.toByte(), STA(a, getIndirectYAddress(), bus), 5) // ページクロスで +1
            //STX
            0x86 -> InstructionInfo(0x86.toByte(), STX(x, getZeroPageAddress(), bus), 3)
            0x8E -> InstructionInfo(0x8E.toByte(), STX(x, getAbsoluteAddress(), bus), 4)
            //STY
            0x8C -> InstructionInfo(0x8C.toByte(), STY(y, getAbsoluteAddress(), bus), 4)
            //TAX
            0xAA -> InstructionInfo(0xAA.toByte(), TAX(a, x, p), 2)
            //TXA
            0x8A -> InstructionInfo(0x8A.toByte(), TXA(a, x, p), 2)
            //TAY
            //TYA
            0x9A -> InstructionInfo(0x9A.toByte(), TXS(x, sp), 2)
            //TSX
            //PHA
            0x48 -> InstructionInfo(0x48, PHA(a, sp, bus), 3)
            //PLA
            0x68 -> InstructionInfo(0x68.toByte(), PLA(a, sp, p, bus), 4)
            //PHP
            //PLP

            else -> error("[${opcode.toString(16)}] not implemented")
        }
    }
}