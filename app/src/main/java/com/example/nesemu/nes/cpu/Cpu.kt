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

    private fun getAbsoluteXAddress() = getAbsoluteAddress() + x.value

    private fun getAbsoluteYAddress() = getAbsoluteAddress() + y.value

    private fun getImmediateValue() = read(pc.address++)

    private fun getXIndexedAbsoluteAddress() : Address = getAbsoluteAddress() + x.value

    private fun getYIndexedAbsoluteAddress() : Address = getAbsoluteAddress() + y.value

    private fun getZeroPageAddress() : Address = Address.buildAddress(0, getImmediateValue().toInt())

    private fun getZeroPageXIndexedAddress() : Address = Address.buildAddress(0, getImmediateValue().toInt()) + x.value

    private fun getZeroPageYIndexedAddress() : Address = Address.buildAddress(0, getImmediateValue().toInt()) + y.value

    private fun getIndirectYAddress() : Address = getZeroPageAddress() + y.value

    fun run() : Int {
        if (nmi.hasInterrupt) {
            p.breakFlag = false
            handleInterrupt(Address.buildAddress(0xFFFB), Address.buildAddress(0xFFFA))
            nmi.hasInterrupt = false
            return 8
        } else {
            if (p.breakFlag /* or irq.hasInterrupt */) { // TODO: IRQの処理の追加
                handleInterrupt(Address.buildAddress(0xFFFE), Address.buildAddress(0xFFFF))
            }
        }
        val instInfo = fetch()
        instInfo.inst.exec()
        return instInfo.cycle +
                if (bus.dmaLock) {
                    bus.dmaLock = false
                    514
                } else {
                    0
                }
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
            //ADC
            0x69 -> InstructionInfo(0x69.toByte(), ADC(getImmediateValue(), a, p), 2)
            //SBC
            //AND
            0x29 -> InstructionInfo(0x29.toByte(), AND(Argument.Immediate(getImmediateValue()), a, p), 2)
            0x25 -> InstructionInfo(0x25.toByte(), AND(Argument.ZeroPage(getImmediateValue(), bus), a, p), 3)
            0x35 -> InstructionInfo(0x15.toByte(), AND(Argument.ZeroPageX(getImmediateValue(), x, bus), a, p), 4)
            0x2D -> InstructionInfo(0x0D.toByte(), AND(Argument.Absolute(getAbsoluteAddress(), bus), a, p), 4)
            0x3D -> InstructionInfo(0x1D.toByte(), AND(Argument.AbsoluteX(getAbsoluteAddress(), x, bus), a, p), 4) // ページクロスで + 1
            0x39 -> InstructionInfo(0x19.toByte(), AND(Argument.AbsoluteY(getAbsoluteAddress(), y, bus), a, p), 4) // ページクロスで + 1
            0x21 -> InstructionInfo(0x01.toByte(), AND(Argument.IndexedIndirect(getImmediateValue(), x, bus), a, p), 6)
            0x31 -> InstructionInfo(0x11.toByte(), AND(Argument.IndirectIndexed(getImmediateValue(), y, bus), a, p), 5) // ページクロスで + 1
            //ORA
            0x09 -> InstructionInfo(0x09.toByte(), ORA(Argument.Immediate(getImmediateValue()), a, p), 2)
            0x05 -> InstructionInfo(0x05.toByte(), ORA(Argument.ZeroPage(getImmediateValue(), bus), a, p), 3)
            0x15 -> InstructionInfo(0x15.toByte(), ORA(Argument.ZeroPageX(getImmediateValue(), x, bus), a, p), 4)
            0x0D -> InstructionInfo(0x0D.toByte(), ORA(Argument.Absolute(getAbsoluteAddress(), bus), a, p), 4)
            0x1D -> InstructionInfo(0x1D.toByte(), ORA(Argument.AbsoluteX(getAbsoluteAddress(), x, bus), a, p), 4) // ページクロスで + 1
            0x19 -> InstructionInfo(0x19.toByte(), ORA(Argument.AbsoluteY(getAbsoluteAddress(), y, bus), a, p), 4) // ページクロスで + 1
            0x01 -> InstructionInfo(0x01.toByte(), ORA(Argument.IndexedIndirect(getImmediateValue(), x, bus), a, p), 6)
            0x11 -> InstructionInfo(0x11.toByte(), ORA(Argument.IndirectIndexed(getImmediateValue(), y, bus), a, p), 5) // ページクロスで + 1
            //EOR
            0x49 -> InstructionInfo(0x49.toByte(), EOR(Argument.Immediate(getImmediateValue()), a, p), 2)
            0x45 -> InstructionInfo(0x45.toByte(), EOR(Argument.ZeroPage(getImmediateValue(), bus), a, p), 3)
            0x55 -> InstructionInfo(0x55.toByte(), EOR(Argument.ZeroPageX(getImmediateValue(), x, bus), a, p), 4)
            0x4D -> InstructionInfo(0x4D.toByte(), EOR(Argument.Absolute(getAbsoluteAddress(), bus), a, p), 4)
            0x5D -> InstructionInfo(0x5D.toByte(), EOR(Argument.AbsoluteX(getAbsoluteAddress(), x, bus), a, p), 4) // ページクロスで + 1
            0x59 -> InstructionInfo(0x59.toByte(), EOR(Argument.AbsoluteY(getAbsoluteAddress(), y, bus), a, p), 4) // ページクロスで + 1
            0x41 -> InstructionInfo(0x41.toByte(), EOR(Argument.IndexedIndirect(getImmediateValue(), x, bus), a, p), 6)
            0x51 -> InstructionInfo(0x51.toByte(), EOR(Argument.IndirectIndexed(getImmediateValue(), y, bus), a, p), 5) // ページクロスで + 1
            //ASL
            //LSR
            0x4A -> InstructionInfo(0x4A, LSR(Argument.Accumulator(a), p), 2)
            0x46 -> InstructionInfo(0x46, LSR(Argument.ZeroPage(getImmediateValue(), bus), p), 5)
            0x56 -> InstructionInfo(0x56, LSR(Argument.ZeroPageX(getImmediateValue(), x, bus), p), 6)
            0x4E -> InstructionInfo(0x4E, LSR(Argument.Absolute(getAbsoluteAddress(), bus), p), 6)
            0x5E -> InstructionInfo(0x5E, LSR(Argument.AbsoluteX(getAbsoluteAddress(), x, bus), p), 6) // ページクロスで + 1
            //ROL
            0x2A -> InstructionInfo(0x2A, ROL(Argument.Accumulator(a), p), 2)
            0x26 -> InstructionInfo(0x26, ROL(Argument.ZeroPage(getImmediateValue(), bus), p), 5)
            0x36 -> InstructionInfo(0x36, ROL(Argument.ZeroPageX(getImmediateValue(), x, bus), p), 6)
            0x2E -> InstructionInfo(0x2E, ROL(Argument.Absolute(getAbsoluteAddress(), bus), p), 6)
            0x3E -> InstructionInfo(0x3E, ROL(Argument.AbsoluteX(getAbsoluteAddress(), x, bus), p), 6) // + 1
            //ROR
            0x6A -> InstructionInfo(0x6A, ROR(Argument.Accumulator(a), p), 2)
            0x66 -> InstructionInfo(0x66, ROR(Argument.ZeroPage(getImmediateValue(), bus), p), 5)
            0x76 -> InstructionInfo(0x76, ROR(Argument.ZeroPageX(getImmediateValue(), x, bus), p), 6)
            0x6E -> InstructionInfo(0x6E, ROR(Argument.Absolute(getAbsoluteAddress(), bus), p), 6)
            0x7E -> InstructionInfo(0x7E, ROR(Argument.AbsoluteX(getAbsoluteAddress(), x, bus), p), 6) // + 1
            //BCC
            0x90 -> InstructionInfo(0x90.toByte(), BCC(pc,  p, getImmediateValue()), 2)
            //BCS
            0xB0 -> InstructionInfo(0xB0.toByte(), BCS(pc,  p, getImmediateValue()), 2)
            //BEQ
            0xF0 -> InstructionInfo(0xF0.toByte(), BEQ(pc, p, getImmediateValue()), 2)
            //BNE
            0xD0 -> InstructionInfo(0xD0.toByte(), BNE(pc, p, getImmediateValue()), 2) // +1 or 2 ブランチで+1 ページクロスで+2
            //BVC
            0x50 -> InstructionInfo(0x50.toByte(), BVC(pc, p, getImmediateValue()), 2) // +1 or 2 ブランチで+1 ページクロスで+2
            //BVS
            0x70 -> InstructionInfo(0x70.toByte(), BVS(pc, p, getImmediateValue()), 2) // +1 or 2 ブランチで+1 ページクロスで+2
            //BPL
            0x10 -> InstructionInfo(0x10, BPL(pc, p, getImmediateValue()), 2) // +1 or 2 ブランチで+1 ページクロスで+2
            //BMI
            0x30 -> InstructionInfo(0x30, BMI(pc, p, getImmediateValue()), 2) // +1 or 2 ブランチで+1 ページクロスで+2
            //BIT
            0x24 -> InstructionInfo(0x24, BIT(read(getZeroPageAddress()), a, p), 3)
            0x2C -> InstructionInfo(0x2C, BIT(read(getAbsoluteAddress()), a, p), 4)
            //JMP
            0x4C -> InstructionInfo(0x4C, JMP(getAbsoluteAddress(), pc), 3)
            //JSR
            0x20 -> InstructionInfo(0x20, JSR(pc, sp, getAbsoluteAddress(), bus), 6)
            //RTS
            0x60 -> InstructionInfo(0x60, RTS(pc, sp, bus), 6)
            //BRK
            0x00 -> InstructionInfo(0x0, BRK(p), 7)
            //RTI
            0x40 -> InstructionInfo(0x40.toByte(), RTI(pc, sp, p, bus), 6)
            //CMP
            0xC9 -> InstructionInfo(0xC9.toByte(), CMP(getImmediateValue(), a, p), 2)
            0xC5 -> InstructionInfo(0xC9.toByte(), CMP(read(getZeroPageAddress()), a, p), 3)
            //CPX
            0xE0 -> InstructionInfo(0xE0.toByte(), CPX(getImmediateValue(), x, p), 2)
            //CPY
            0xC0 -> InstructionInfo(0xC0.toByte(), CPY(getImmediateValue(), y, p), 2)
            //INC
            0xE6 -> InstructionInfo(0xE6.toByte(), INC(p, getZeroPageAddress(), bus), 5)
            0xEE -> InstructionInfo(0xEE.toByte(), INC(p, getAbsoluteAddress(), bus), 6)
            //DEC
            0xC6 -> InstructionInfo(0xC6.toByte(), DEC(p, getZeroPageAddress(), bus), 5)
            0xCE -> InstructionInfo(0xCE.toByte(), DEC(p, getAbsoluteAddress(), bus), 6)
            //INX
            0xE8 -> InstructionInfo(0xE8.toByte(), INX(x, p),2)
            //DEX
            0xCA -> InstructionInfo(0xCA.toByte(), DEX(x, p), 2)
            //INY
            0xC8 -> InstructionInfo(0xC8.toByte(), INY(y, p),2)
            //DEY
            0x88 -> InstructionInfo(0x88.toByte(), DEY(y, p), 2)
            //CLC
            0x18 -> InstructionInfo(0x18.toByte(), CLC(p), 2)
            //SEC
            0x38 -> InstructionInfo(0x38.toByte(), SEC(p), 2)
            //CLI
            0x58 -> InstructionInfo(0x58, CLI(p), 2)
            //SEI
            0x78 -> InstructionInfo(0x78, SEI(p), 2)
            //CLD
            0xd8 -> InstructionInfo(0xd8.toByte(), CLD(p), 2)
            //SED
            0xF8 -> InstructionInfo(0xF8.toByte(), SED(p), 2)
            //CLV
            0xB8 -> InstructionInfo(0xB8.toByte(), CLV(p), 2)
            //LDA
            0xA9 -> InstructionInfo(0xA9.toByte(), LDA(getImmediateValue(), a, p), 2)
            0xA5 -> InstructionInfo(0xA9.toByte(), LDA(read(getZeroPageAddress()), a, p), 3)
            0xAD -> InstructionInfo(0xAD.toByte(), LDA(read(getAbsoluteAddress()), a, p), 4)
            0xBD -> InstructionInfo(0xBD.toByte(), LDA(read(getXIndexedAbsoluteAddress()), a, p), 4) // ページクロスで+1
            0xB9 -> InstructionInfo(0xBD.toByte(), LDA(read(getYIndexedAbsoluteAddress()), a, p), 4) // ページクロスで+1
            0xB1 -> InstructionInfo(0xB1.toByte(), LDA(read(getIndirectYAddress()), a, p), 5) // ページクロスで+1
            0xBE -> InstructionInfo(0xBE.toByte(), LDA(read(getAbsoluteYAddress()), a, p), 4) // ページクロスで+1
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
            0xA8 -> InstructionInfo(0xA8.toByte(), TAY(a, y, p), 2)
            //TYA
            0x98 -> InstructionInfo(0x98.toByte(), TYA(a, y, p), 2)
            //TXS
            0x9A -> InstructionInfo(0x9A.toByte(), TXS(x, sp), 2)
            //TSX
            0xBA -> InstructionInfo(0xBA.toByte(), TSX(x, sp, p), 2)
            //PHA
            0x48 -> InstructionInfo(0x48, PHA(a, sp, bus), 3)
            //PLA
            0x68 -> InstructionInfo(0x68.toByte(), PLA(a, sp, p, bus), 4)
            //PHP
            0x08 -> InstructionInfo(0x08, PHP(p, sp, bus), 3)
            //PLP
            0x28 -> InstructionInfo(0x28, PLP(p, sp, bus), 4)

            else -> error("[${opcode.toString(16)}] not implemented")
        }
    }
}