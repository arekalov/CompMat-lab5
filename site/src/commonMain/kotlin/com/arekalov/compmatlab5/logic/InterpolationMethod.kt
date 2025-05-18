package com.arekalov.compmatlab5.logic

import com.arekalov.compmatlab5.models.DataPoint

interface InterpolationMethod {
    fun interpolate(points: List<DataPoint>, x0: Double): Double
}
