package com.arekalov.compmatlab5.logic

import com.arekalov.compmatlab5.logic.InterpolationMethodsBase.factorial
import com.arekalov.compmatlab5.logic.InterpolationMethodsBase.finiteDifferences
import com.arekalov.compmatlab5.models.DataPoint

fun gaussInterpolate(points: List<DataPoint>, x0: Double): Double {
    val n = points.size
    val alphaInd = n / 2
    val finDifs = finiteDifferences(points)
    val h = points[1].x - points[0].x
    val dts1 = listOf(0, -1, 1, -2, 2, -3, 3, -4, 4)
    val yAlpha = points[alphaInd].y
    val sum = (1 until n).sumOf { k ->
        val size = finDifs[k].size
        val centerIndex = if (x0 > points[alphaInd].x) {
            size / 2
        } else {
            size / 2 - (1 - size % 2)
        }
        val safeIndex = centerIndex.coerceIn(0, size - 1)
        val term = (0 until k).fold(1.0) { acc, j ->
            acc * if (x0 > points[alphaInd].x) {
                (x0 - points[alphaInd].x) / h + dts1[j]
            } else {
                (x0 - points[alphaInd].x) / h - dts1[j]
            }
        }
        val coeff = finDifs[k][safeIndex]
        (term * coeff) / factorial(k)
    }
    return yAlpha + sum
}