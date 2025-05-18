package com.arekalov.compmatlab5.logic

import com.arekalov.compmatlab5.models.DataPoint

fun gaussInterpolate(points: List<DataPoint>, x0: Double): Double {
    val n = points.size - 1
    val alphaInd = InterpolationMethodsBase.getMiddleIndex(points)
    val finDifs = InterpolationMethodsBase.finiteDifferences(points)
    val h = InterpolationMethodsBase.getStep(points)
    val dts1 = listOf(0, -1, 1, -2, 2, -3, 3, -4, 4)
    
    if (x0 > points[alphaInd].x) {
        return points[alphaInd].y + (1..n).sumOf { k ->
            var term = finDifs[k][finDifs[k].size / 2] / InterpolationMethodsBase.factorial(k)
            for (j in 0 until k) {
                term *= (x0 - points[alphaInd].x) / h + dts1[j]
            }
            term
        }
    } else {
        return points[alphaInd].y + (1..n).sumOf { k ->
            var term = finDifs[k][finDifs[k].size / 2 - (1 - finDifs[k].size % 2)] / InterpolationMethodsBase.factorial(k)
            for (j in 0 until k) {
                term *= (x0 - points[alphaInd].x) / h - dts1[j]
            }
            term
        }
    }
}
