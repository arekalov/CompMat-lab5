package com.arekalov.compmatlab5.logic

import com.arekalov.compmatlab5.models.DataPoint

fun newtonDividedDifferenceInterpolate(points: List<DataPoint>, x0: Double): Double {
    val coef = InterpolationMethodsBase.dividedDifferences(points)
    var result = points[0].y
    
    for (k in 1 until points.size) {
        var term = coef[k]
        for (j in 0 until k) {
            term *= (x0 - points[j].x)
        }
        result += term
    }
    
    return result
}
