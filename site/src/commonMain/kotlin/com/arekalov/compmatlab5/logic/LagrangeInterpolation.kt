package com.arekalov.compmatlab5.logic

import com.arekalov.compmatlab5.models.DataPoint

object LagrangeInterpolation : InterpolationMethod {
    override fun interpolate(points: List<DataPoint>, x0: Double): Double {
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
        
        return result
    }
} 