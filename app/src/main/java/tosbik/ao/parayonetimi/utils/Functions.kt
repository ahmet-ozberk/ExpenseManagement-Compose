package tosbik.ao.parayonetimi.utils

import java.text.DecimalFormat

object Functions {
    fun splitDouble(n: Double? = 0.0): Pair<String, String> {
        val number = n ?: 0.0
        val integerFormat = DecimalFormat("#,###")
        val integerPart = integerFormat.format(number.toInt())

        val decimalFormat = DecimalFormat("00")
        val decimalPart = decimalFormat.format(((number - number.toInt()) * 100).toInt())

        return Pair(integerPart, decimalPart.replace("-",""))
    }
}