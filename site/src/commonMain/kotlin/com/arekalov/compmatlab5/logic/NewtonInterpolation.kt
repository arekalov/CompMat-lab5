package com.arekalov.compmatlab5.logic

import com.arekalov.compmatlab5.models.DataPoint

fun newtonInterpolate(points: List<DataPoint>, x0: Double): Double {
    val n = points.size
    val table = InterpolationLogicController.buildFiniteDifferenceTable(points)
    val x = table.x
    val diff = table.differences
    var result = diff[0][0]
    var mult = 1.0
    for (i in 1 until n) {
        mult *= (x0 - x[i - 1])
        result += diff[0][i] / InterpolationLogicController.factorial(i) * mult
    }
    return result
} 