package com.arekalov.compmatlab5.logic

import com.arekalov.compmatlab5.models.DataPoint

fun besselInterpolate(points: List<DataPoint>, x0: Double): Double {
    val n = points.size - 1
    val alphaInd = InterpolationMethodsBase.getMiddleIndex(points)
    val finDifs = InterpolationMethodsBase.finiteDifferences(points)
    val h = InterpolationMethodsBase.getStep(points)
    val dts1 = listOf(0, -1, 1, -2, 2, -3, 3, -4, 4, -5, 5)
    
    return (points[alphaInd].y + points[alphaInd].y) / 2 + (1..n).sumOf { k ->
        var term1 = finDifs[k][finDifs[k].size / 2] / InterpolationMethodsBase.factorial(2 * k)
        for (j in 0 until k) {
            term1 *= (x0 - points[alphaInd].x) / h + dts1[j]
        }
        
        var term2 = finDifs[k][finDifs[k].size / 2] / InterpolationMethodsBase.factorial(2 * k + 1)
        term2 *= ((x0 - points[alphaInd].x) / h - 0.5)
        for (j in 0 until k) {
            term2 *= (x0 - points[alphaInd].x) / h + dts1[j]
        }
        
        term1 + term2
    }
}
