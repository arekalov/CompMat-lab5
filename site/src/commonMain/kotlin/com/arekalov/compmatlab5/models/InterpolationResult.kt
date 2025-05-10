package com.arekalov.compmatlab5.models

import com.varabyte.kobweb.compose.ui.graphics.Color

data class InterpolationResult(
    val methodName: String,
    val value: Double,
    val polynomial: String,
    val colorString: String,
    val invertedColorString: String,
    val color: Color,
    val invertedColor: Color,
) 