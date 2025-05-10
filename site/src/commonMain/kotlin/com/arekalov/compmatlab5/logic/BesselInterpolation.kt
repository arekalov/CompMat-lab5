package com.arekalov.compmatlab5.logic

import com.arekalov.compmatlab5.models.DataPoint
import com.arekalov.compmatlab5.theme.ThemeColors
import com.varabyte.kobweb.compose.ui.graphics.Color
import kotlin.math.pow
import kotlin.math.round

object BesselInterpolation : InterpolationMethod {
    private lateinit var themeColors: ThemeColors

    fun init(colors: ThemeColors) {
        themeColors = colors
    }

    override val colorString: String
        get() = "#${themeColors.warningString}"
    override val invertedColorString: String
        get() = "#${themeColors.warningInversedString}"

    override val color: Color
        get() = themeColors.warningColor
    override val colorInverted: Color
        get() = themeColors.warningInversedColor

    override fun interpolate(points: List<DataPoint>, x0: Double): Pair<Double, String> {
        val n = points.size - 1
        val alphaInd = n / 2
        val finDifs = mutableListOf<List<Double>>()
        finDifs.add(points.map { it.y })

        for (k in 1..n) {
            val last = finDifs.last()
            finDifs.add((0 until n - k + 1).map { i -> last[i + 1] - last[i] })
        }

        val h = points[1].x - points[0].x
        val dts1 = listOf(0, -1, 1, -2, 2, -3, 3, -4, 4, -5, 5)
        val t = (x0 - points[alphaInd].x) / h

        var result = (points[alphaInd].y + points[alphaInd + 1].y) / 2
        for (k in 1..n) {
            var mult1 = 1.0
            var mult2 = 1.0
            for (j in 0 until k) {
                if (j < dts1.size) {
                    mult1 *= (t + dts1[j])
                    mult2 *= (t + dts1[j])
                }
            }
            if (k < finDifs.size && finDifs[k].size > finDifs[k].size / 2) {
                result += mult1 * finDifs[k][finDifs[k].size / 2] / InterpolationLogicController.factorial(2 * k) +
                        (t - 0.5) * mult2 * finDifs[k][finDifs[k].size / 2] / InterpolationLogicController.factorial(2 * k + 1)
            }
        }

        return Pair(result, getPolynomial(points))
    }

    override fun getPolynomial(points: List<DataPoint>): String {
        val n = points.size - 1
        val alphaInd = n / 2
        val finDifs = mutableListOf<List<Double>>()
        finDifs.add(points.map { it.y })

        for (k in 1..n) {
            val last = finDifs.last()
            finDifs.add((0 until n - k + 1).map { i -> last[i + 1] - last[i] })
        }

        val h = points[1].x - points[0].x
        val dts1 = listOf(0, -1, 1, -2, 2, -3, 3, -4, 4, -5, 5)

        val terms = mutableListOf<String>()
        terms.add("(${formatNumber(points[alphaInd].y)} + ${formatNumber(points[alphaInd + 1].y)}) / 2")

        for (k in 1..n) {
            if (k < finDifs.size && finDifs[k].size > finDifs[k].size / 2) {
                val term = buildString {
                    append(" + ${formatNumber(finDifs[k][finDifs[k].size / 2] / InterpolationLogicController.factorial(2 * k))}")
                    for (j in 0 until k) {
                        if (j < dts1.size) {
                            append(" * ((x - ${formatNumber(points[alphaInd].x)})/${formatNumber(h)} + ${dts1[j]})")
                        }
                    }
                    append(" + ((x - ${formatNumber(points[alphaInd].x)})/${formatNumber(h)} - 0.5) * ${formatNumber(finDifs[k][finDifs[k].size / 2] / InterpolationLogicController.factorial(2 * k + 1))}")
                    for (j in 0 until k) {
                        if (j < dts1.size) {
                            append(" * ((x - ${formatNumber(points[alphaInd].x)})/${formatNumber(h)} + ${dts1[j]})")
                        }
                    }
                }
                terms.add(term)
            }
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