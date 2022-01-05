package com.example.nesemu.nes.cpu

import com.example.nesemu.nes.util.Address

sealed class Register {
    class A(var value: Byte = 0.toByte()): Register()
    class X(var value: Byte = 0.toByte()): Register()
    class Y(var value: Byte = 0.toByte()): Register()

    class SP(var address: Address): Register() {
        operator fun plusAssign(arg: Int) {
            this.address += arg
        }

        operator fun plusAssign(arg: Byte) {
            val newAddress = (this.address.value + (arg.toInt() and 0xFF)) and 0xFF
            this.address = Address.buildAddress(0x01, newAddress)
        }

        fun setAddress(data: Byte) {
            this.address = Address.buildAddress(0x01, (data.toInt() and 0xFF))
        }
    }

    class PC(var address: Address): Register() {
        operator fun plusAssign(arg: Int) {
            this.address += arg
        }

        operator fun plusAssign(arg: Byte) {
            this.address += (arg.toInt() + 0xFF)
        }
    }

    data class P(var negative: Boolean = false,
                      var overflow: Boolean = false,
                      var breakFlag: Boolean = false,
                      var decimal: Boolean = false,
                      var interrupt: Boolean = false,
                      var zero: Boolean = false,
                      var carry: Boolean = false): Register() {

        fun toByte(): Byte {
            var value = 0b0010_0000
            if (negative) {
                value = value or 0b1000_0000
            }
            if (overflow) {
                value = value or 0b0100_0000
            }
            if (breakFlag) {
                value = value or 0b0001_0000
            }
            if (decimal) {
                value = value or 0b0000_1000
            }
            if (interrupt) {
                value = value or 0b0000_0100
            }
            if (zero) {
                value = value or 0b0000_0010
            }
            if (carry) {
                value = value or 0b0000_0001
            }
            return value.toByte()
        }

        fun setFlag(data: Byte) {
            val data: Int = data.toInt() and 0xFF
            this.negative = data and 0b1000_0000 != 0
            this.overflow = data and 0b0100_0000 != 0
            this.breakFlag = data and 0b0001_0000 != 0
            this.decimal = data and 0b0000_1000 != 0
            this.interrupt = data and 0b0000_0100 != 0
            this.zero = data and 0b0000_0010 != 0
            this.carry = data and 0b0000_0001 != 0
        }
    }
}
