package com.arekalov.compmatlab5.logic

import com.arekalov.compmatlab5.logic.InterpolationMethodsBase.factorial
import com.arekalov.compmatlab5.logic.InterpolationMethodsBase.finiteDifferences
import com.arekalov.compmatlab5.models.DataPoint

// Вычисляет значение интерполяционного полинома Гаусса в точке x0
// points — список исходных точек (x, y), предполагается что они равноотстоящие
fun gaussInterpolate(points: List<DataPoint>, x0: Double): Double {
    val n = points.size
    val alphaInd = n / 2 // Индекс центральной точки
    val finDifs = finiteDifferences(points) // Таблица конечных разностей
    val h = points[1].x - points[0].x // Шаг между точками
    // dts1 — последовательность для вычисления множителей в формуле Гаусса
    val dts1 = listOf(0, -1, 1, -2, 2, -3, 3, -4, 4)
    val yAlpha = points[alphaInd].y // Значение функции в центральной точке
    // Суммируем члены полинома Гаусса по порядку разности
    val sum = (1 until n).sumOf { k ->
        val size = finDifs[k].size
        // Выбираем центральный индекс для разности нужного порядка
        val centerIndex = if (x0 > points[alphaInd].x) {
            size / 2
        } else {
            size / 2 - (1 - size % 2)
        }
        // Гарантируем, что индекс не выйдет за границы
        val safeIndex = centerIndex.coerceIn(0, size - 1)
        // Вычисляем множитель (t + dts1[j]) или (t - dts1[j])
        val term = (0 until k).fold(1.0) { acc, j ->
            acc * if (x0 > points[alphaInd].x) {
                (x0 - points[alphaInd].x) / h + dts1[j]
            } else {
                (x0 - points[alphaInd].x) / h - dts1[j]
            }
        }
        val coeff = finDifs[k][safeIndex] // Берём нужную разность
        (term * coeff) / factorial(k) // Делим на факториал порядка
    }
    return yAlpha + sum // Складываем с центральным значением
}