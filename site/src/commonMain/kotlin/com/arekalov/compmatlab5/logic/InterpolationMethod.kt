package com.arekalov.compmatlab5.logic

import com.arekalov.compmatlab5.models.DataPoint
import com.varabyte.kobweb.compose.ui.graphics.Color

interface InterpolationMethod {
    val color: Color
    val colorInverted: Color
    val colorString: String
    val invertedColorString: String
    fun interpolate(points: List<DataPoint>, x0: Double): Pair<Double, String>
    fun getPolynomial(points: List<DataPoint>): String
} 