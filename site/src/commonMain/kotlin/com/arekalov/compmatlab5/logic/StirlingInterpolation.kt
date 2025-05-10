package com.arekalov.compmatlab5.logic

import com.arekalov.compmatlab5.models.DataPoint
import com.arekalov.compmatlab5.theme.ThemeColors
import com.varabyte.kobweb.compose.ui.graphics.Color
import kotlin.math.pow
import kotlin.math.round

object StirlingInterpolation : InterpolationMethod {
    private lateinit var themeColors: ThemeColors

    fun init(colors: ThemeColors) {
        themeColors = colors
    }

    override val colorString: String
        get() = "#${themeColors.secondaryString}"
    override val invertedColorString: String
        get() = "#${themeColors.secondaryInversedString}"

    override val color: Color
        get() = themeColors.secondaryColor
    override val colorInverted: Color
        get() = themeColors.secondaryInversedColor

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
        val dts1 = listOf(0, -1, 1, -2, 2, -3, 3, -4, 4)
        val t = (x0 - points[alphaInd].x) / h

        val f1 = calculateF1(points, finDifs, alphaInd, h, dts1, t)
        val f2 = calculateF2(points, finDifs, alphaInd, h, dts1, t)
        val result = (f1 + f2) / 2

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
        val dts1 = listOf(0, -1, 1, -2, 2, -3, 3, -4, 4)

        val terms = mutableListOf<String>()
        terms.add(formatNumber(points[alphaInd].y))

        for (k in 1..n) {
            val term = buildString {
                append(" + ${formatNumber(finDifs[k][finDifs[k].size / 2] / InterpolationLogicController.factorial(k))}")
                for (j in 0 until k) {
                    append(" * ((x - ${formatNumber(points[alphaInd].x)})/${formatNumber(h)} + ${dts1[j]})")
                }
            }
            terms.add(term)
        }

        return "y = " + terms.joinToString("")
    }

    private fun calculateF1(
        points: List<DataPoint>,
        finDifs: List<List<Double>>,
        alphaInd: Int,
        h: Double,
        dts1: List<Int>,
        t: Double
    ): Double {
        var result = points[alphaInd].y
        for (k in 1..points.size - 1) {
            var mult = 1.0
            for (j in 0 until k) {
                mult *= (t + dts1[j])
            }
            result += mult * finDifs[k][finDifs[k].size / 2] / InterpolationLogicController.factorial(k)
        }
        return result
    }

    private fun calculateF2(
        points: List<DataPoint>,
        finDifs: List<List<Double>>,
        alphaInd: Int,
        h: Double,
        dts1: List<Int>,
        t: Double
    ): Double {
        var result = points[alphaInd].y
        for (k in 1..points.size - 1) {
            var mult = 1.0
            for (j in 0 until k) {
                mult *= (t - dts1[j])
            }
            result += mult * finDifs[k][finDifs[k].size / 2 - (1 - finDifs[k].size % 2)] / InterpolationLogicController.factorial(k)
        }
        return result
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