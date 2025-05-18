package com.arekalov.compmatlab5.logic

import com.arekalov.compmatlab5.models.DataPoint

interface Interpolated {
    fun interpolate(points: List<DataPoint>, x0: Double): Pair<Double, String>
}
