package com.arekalov.compmatlab5.logic

import com.arekalov.compmatlab5.models.DataPoint
import com.arekalov.compmatlab5.models.FiniteDifferenceTable

object InterpolationLogicController {
    /**
     * Строит таблицу конечных разностей для заданных точек.
     */
    fun buildFiniteDifferenceTable(points: List<DataPoint>): FiniteDifferenceTable {
        val n = points.size
        val x = points.map { it.x }
        val y = points.map { it.y }
        val differences = MutableList(n) { MutableList(n) { 0.0 } }
        // Заполняем первый столбец значениями y
        for (i in 0 until n) {
            differences[i][0] = y[i]
        }
        // Вычисляем конечные разности
        for (j in 1 until n) {
            for (i in 0 until n - j) {
                differences[i][j] = differences[i + 1][j - 1] - differences[i][j - 1]
            }
        }
        // Обрезаем лишние элементы в каждой строке
        val trimmed = differences.mapIndexed { i, row -> row.take(n - i) }
        return FiniteDifferenceTable(x, trimmed)
    }

    fun factorial(n: Int): Double = if (n <= 1) 1.0 else n * factorial(n - 1)

    fun newton(points: List<DataPoint>, x0: Double): Double = newtonInterpolate(points, x0)

    fun lagrange(points: List<DataPoint>, x0: Double): Double = lagrangeInterpolate(points, x0)
} 