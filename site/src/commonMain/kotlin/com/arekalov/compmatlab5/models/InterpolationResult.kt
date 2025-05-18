package com.arekalov.compmatlab5.models

data class InterpolationResult(
    val method: InterpolationMethod,
    val value: Double,
    val points: List<DataPoint>,
) 