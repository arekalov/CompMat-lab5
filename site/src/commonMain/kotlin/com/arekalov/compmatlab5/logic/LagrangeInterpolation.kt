package com.arekalov.compmatlab5.logic

import com.arekalov.compmatlab5.models.DataPoint
import com.arekalov.compmatlab5.theme.ThemeColors
import com.varabyte.kobweb.compose.ui.graphics.Color
import kotlin.math.pow
import kotlin.math.round

object LagrangeInterpolation : InterpolationMethod {
    private lateinit var themeColors: ThemeColors

    fun init(colors: ThemeColors) {
        themeColors = colors
    }

    override val colorString: String
        get() = "#${themeColors.errorString}"
    override val invertedColorString: String
        get() = "#${themeColors.errorIversedString}"

    override val color: Color
        get() = themeColors.errorColor
    override val colorInverted: Color
        get() = themeColors.errorInversedColor

    override fun interpolate(points: List<DataPoint>, x0: Double): Pair<Double, String> {
        val n = points.size
        var result = 0.0
        
        for (i in 0 until n) {
            var term = points[i].y
            for (j in 0 until n) {
                if (i != j) {
                    term *= (x0 - points[j].x) / (points[i].x - points[j].x)
                }
            }
            result += term
        }
        
        return Pair(result, getPolynomial(points))
    }

    override fun getPolynomial(points: List<DataPoint>): String {
        val n = points.size
        val terms = mutableListOf<String>()
        
        for (i in 0 until n) {
            val term = buildString {
                append("${formatNumber(points[i].y)}")
                for (j in 0 until n) {
                    if (i != j) {
                        append(" * (x - ${formatNumber(points[j].x)}) / (${formatNumber(points[i].x)} - ${formatNumber(points[j].x)})")
                    }
                }
            }
            terms.add(term)
        }
        
        return "y = " + terms.joinToString(" + ")
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