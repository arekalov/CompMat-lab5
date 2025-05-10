package com.arekalov.compmatlab5.logic

import com.arekalov.compmatlab5.models.DataPoint
import com.arekalov.compmatlab5.theme.ThemeColors
import com.varabyte.kobweb.compose.ui.graphics.Color
import kotlin.math.pow
import kotlin.math.round

object NewtonInterpolation : InterpolationMethod {
    private lateinit var themeColors: ThemeColors

    fun init(colors: ThemeColors) {
        themeColors = colors
    }

    override val colorString: String
        get() = "#${themeColors.successString}"
    override val invertedColorString: String
        get() = "#${themeColors.successSInversedtSring}"

    override val color: Color
        get() = themeColors.successColor
    override val colorInverted: Color
        get() = themeColors.successInversedColor

    override fun interpolate(points: List<DataPoint>, x0: Double): Pair<Double, String> {
        val n = points.size
        val table = InterpolationLogicController.buildFiniteDifferenceTable(points)
        val h = points[1].x - points[0].x
        val t = (x0 - points[0].x) / h
        
        var result = table.differences[0][0]
        var mult = 1.0
        for (i in 1 until n) {
            mult *= (t - (i - 1))
            result += table.differences[0][i] / InterpolationLogicController.factorial(i) * mult
        }

        return Pair(result, getPolynomial(points))
    }

    override fun getPolynomial(points: List<DataPoint>): String {
        val n = points.size
        val table = InterpolationLogicController.buildFiniteDifferenceTable(points)
        val h = points[1].x - points[0].x
        
        val terms = mutableListOf<String>()
        terms.add(formatNumber(table.differences[0][0]))
        
        for (i in 1 until n) {
            val term = buildString {
                append(" + ${formatNumber(table.differences[0][i] / InterpolationLogicController.factorial(i))}")
                for (j in 0 until i) {
                    append(" * (x - ${formatNumber(points[j].x)})")
                }
            }
            terms.add(term)
        }
        
        return "y = " + terms.joinToString("")
    }

    private fun formatNumber(number: Double): String {
        return if (kotlin.math.abs(number) < 1e-10) "0" else formatNumber(number, 2)
    }

    private fun formatNumber(number: Double, precision: Int): String {
        val factor = 10.0.pow(precision)
        val roundedNumber = round(number * factor) / factor
        return roundedNumber.toString()
    }
}