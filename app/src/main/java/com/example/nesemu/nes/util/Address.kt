package com.example.nesemu.nes.util

class Address private constructor(data: Int) {
    var value: Int = data

    companion object {
        fun buildAddress(upperAddress: Int, lowerAddress: Int): Address {
            val data = (((upperAddress shl 8) and 0xFF00) or (lowerAddress and 0xFF)) and 0xFFFF
            return Address(data)
        }
    }

    operator fun plusAssign(arg: Byte) {
        this.value += arg.toInt() and 0xFF
    }

    operator fun plus(arg: Byte) : Address {
        val newAddress = this.value + arg.toInt() and 0xFF
        return Address(newAddress)
    }

    operator fun plusAssign(arg: Int) {
        this.value += arg
    }

    operator fun minusAssign(arg: Byte) {
        this.value -= arg.toInt() and 0xFF
    }

    operator fun minusAssign(arg: Int) {
        this.value -= arg
    }

    operator fun remAssign(arg: Int) {
        this.value %= arg
    }

    operator fun inc() : Address = Address(value + 1)

    override fun toString(): String {
        return "0x" + value.toString(16)
    }
}