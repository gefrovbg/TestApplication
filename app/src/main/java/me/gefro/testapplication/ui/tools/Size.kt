package me.gefro.testapplication.ui.tools

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToLong

fun formatBinarySize(
    byteCount: Long,
    decimalPlaces: Int = 2,
    zeroPadFraction: Boolean = false
): String {
    require(byteCount>Long.MIN_VALUE) { "Out of range" }
    require(decimalPlaces>=0) { "Negative decimal places unsupported" }
    val isNegative = byteCount < 0
    val absByteCount = abs(byteCount)
    return if (absByteCount < 1024) {
        "$byteCount B"
    } else {
        val zeroBitCount: Int = (63 - absByteCount.countLeadingZeroBits()) / 10
        val absNumber: Double = absByteCount.toDouble() / (1L shl zeroBitCount * 10)
        val roundingFactor: Int = 10.0.pow(decimalPlaces).toInt()
        val absRoundedNumberString = with((absNumber * roundingFactor).roundToLong().toString()) {
            val splitIndex = length - decimalPlaces - 1
            val wholeString = substring(0..splitIndex)
            val fractionString = with(substring(splitIndex + 1)) {
                if (zeroPadFraction) this else dropLastWhile { digit -> digit == '0' }
            }
            if (fractionString.isEmpty()) wholeString else "$wholeString.$fractionString"
        }
        val roundedNumberString = if(isNegative) "-$absRoundedNumberString" else absRoundedNumberString
        "$roundedNumberString ${"KMGTPE"[zeroBitCount - 1]}B"
    }
}