package com.arekalov.compmatlab5.logic

import com.arekalov.compmatlab5.models.DataPoint

fun newtonFiniteDifferenceInterpolate(points: List<DataPoint>, x0: Double): Double {
    val h = InterpolationMethodsBase.getStep(points)
    val deltaY = InterpolationMethodsBase.finiteDifferences(points)
    var result = points[0].y
    
    for (k in 1 until points.size) {
        var term = deltaY[0][k] / InterpolationMethodsBase.factorial(k)
        for (j in 0 until k) {
            term *= (x0 - points[0].x) / h - j
        }
        result += term
    }
    
    return result
}
