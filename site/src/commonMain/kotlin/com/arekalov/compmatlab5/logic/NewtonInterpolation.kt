package com.arekalov.compmatlab5.logic

import com.arekalov.compmatlab5.models.DataPoint

fun newtonInterpolate(points: List<DataPoint>, x0: Double): Double {
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
    return result
}