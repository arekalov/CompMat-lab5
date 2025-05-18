package com.arekalov.compmatlab5.models

import com.varabyte.kobweb.compose.ui.graphics.Color

data class InterpolationResult(
    val methodName: String,
    val value: Double,
    val points: List<DataPoint>,
) 