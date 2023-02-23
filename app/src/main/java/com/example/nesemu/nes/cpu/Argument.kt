package com.example.nesemu.nes.cpu

import com.example.nesemu.nes.Bus
import com.example.nesemu.nes.util.Address
import java.util.*

sealed class Argument {
    interface Writable {
        fun write(data: Byte)
    }

    interface Readable {
        fun read() : Byte
    }

    interface ReadWritable: Readable, Writable

    abstract override fun toString(): String

    object Implied: Argument() {
        override fun toString(): String = ""
    }

    class Accumulator(private val a: Register.A): ReadWritable, Argument() {
        override fun read(): Byte = a.value
        override fun write(data: Byte) {
            a.value = data
        }
        override fun toString(): String = "A"
    }

    class Immediate(private val value: Byte): Readable, Argument() {
        override fun read(): Byte = value
        override fun toString(): String = "#$${(value.toInt() and 0xFF).toString(16)
            .uppercase(Locale.getDefault()).padStart(2, '0')}"
    }

    class ZeroPage(private val immediateVal: Byte, private val bus: Bus): ReadWritable, Argument() {
        private val address: Address = Address.buildAddress(0, immediateVal.toInt())
        override fun read(): Byte = bus.read(address)
        override fun write(data: Byte) = bus.write(address, data)
        override fun toString(): String = "$${immediateVal.toString(16)
            .uppercase()
            .padStart(2, '0')}"
    }

    class ZeroPageX(private val immediateVal: Byte, private val x: Register.X, private val bus: Bus)
        : ReadWritable, Argument() {
        private val address: Address = Address.buildAddress(0, immediateVal.toInt())
        override fun read(): Byte = bus.read(address + x.value)
        override fun write(data: Byte) = bus.write(address + x.value, data)
        override fun toString(): String = "$${immediateVal.toString(16)
            .uppercase()
            .padStart(2, '0')},X"
    }

    class ZeroPageY(private val immediateVal: Byte, private val y: Register.Y, private val bus: Bus)
        : ReadWritable, Argument() {
        private val address: Address = Address.buildAddress(0, immediateVal.toInt())
        override fun read(): Byte = bus.read(address + y.value)
        override fun write(data: Byte) = bus.write(address + y.value, data)
        override fun toString(): String = "$${immediateVal.toString(16)
            .uppercase()
            .padStart(2, '0')},Y"
    }


    class Absolute(val address: Address, private val bus: Bus)
        : ReadWritable, Argument() {
        override fun read(): Byte = bus.read(address)
        override fun write(data: Byte) = bus.write(address, data)
        override fun toString(): String = "$${address}"
    }

    class AbsoluteX(private val address: Address, private val x: Register.X, private val bus: Bus)
        : ReadWritable, Argument() {
        override fun read(): Byte = bus.read(address + x.value)
        override fun write(data: Byte) = bus.write(address + x.value, data)
        override fun toString(): String = "$${address},X"
    }

    class AbsoluteY(private val address: Address, private val y: Register.Y, private val bus: Bus)
        : ReadWritable, Argument() {
        override fun read(): Byte = bus.read(address + y.value)
        override fun write(data: Byte) = bus.write(address + y.value, data)
        override fun toString(): String = "$${address},Y"
    }

    class Indirect(private val indirectAddress: Address, bus: Bus)
        : Argument() {
        val address: Address
        init {
            val lower = bus.read(indirectAddress)
            val upper = bus.read(indirectAddress + 1)
            address = Address.buildAddress(upper.toInt(), lower.toInt())
        }

        override fun toString(): String = "($${indirectAddress})"
    }

    class IndexedIndirect(private val base: Byte, x: Register.X, private val bus: Bus)
        : ReadWritable, Argument() {
        private val address: Address
        init {
            val indirectAddress = Address.buildAddress(0, base + x.value)
            val lower = bus.read(indirectAddress)
            val upper = bus.read(indirectAddress + 1)
            address = Address.buildAddress(upper.toInt(), lower.toInt())
        }

        override fun write(data: Byte) = bus.write(address, data)
        override fun read(): Byte = bus.read(address)
        override fun toString(): String = "($${base.toString(16).uppercase()
            .padStart(2, '0')},X)"
    }

    class IndirectIndexed(private val base: Byte, y: Register.Y, private val bus: Bus)
        : ReadWritable, Argument() {
            private val address: Address
            init {
                val indirectAddress = Address.buildAddress(0, base.toInt())
                val lower = bus.read(indirectAddress)
                val upper = bus.read(indirectAddress + 1)
                address = Address.buildAddress(upper.toInt(), lower.toInt()) + y.value
            }

        override fun write(data: Byte) = bus.write(address, data)
        override fun read(): Byte = bus.read(address)
        override fun toString(): String = "($${base.toString(16)
            .uppercase().padStart(2, '0')}),Y"
    }
}

