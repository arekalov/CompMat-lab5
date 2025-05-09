package com.arekalov.compmatlab5.models

data class InterpolationResult(
    val method: String, // например, "Ньютон", "Гаусс"
    val value: Double, // результат интерполяции
    val coefficients: List<Double> = emptyList(), // коэффициенты многочлена
    val error: Double? = null, // ошибка (если есть)
    val details: String? = null // дополнительные сведения
) 