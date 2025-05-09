package com.arekalov.compmatlab5.models

data class InterpolationResult(
    val lagrangeValue: Double, // результат интерполяции методом Лагранжа
    val newtonValue: Double, // результат интерполяции методом Ньютона
    val tValue: Double, // значение t для обоих методов
    val error: String? = null // ошибка (если есть)
) 