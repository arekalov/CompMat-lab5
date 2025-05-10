package com.arekalov.compmatlab5.logic

import com.arekalov.compmatlab5.models.DataPoint
import com.arekalov.compmatlab5.models.FiniteDifferenceTable
import kotlin.math.abs

object InterpolationLogicController {
    fun factorial(n: Int): Int {
        return if (n <= 1) 1 else n * factorial(n - 1)
    }

    fun buildFiniteDifferenceTable(points: List<DataPoint>): FiniteDifferenceTable {
        val n = points.size
        val differences = Array(n) { DoubleArray(n) { 0.0 } }

        // Заполняем первый столбец значениями y
        for (i in 0 until n) {
            differences[i][0] = points[i].y
        }

        // Вычисляем конечные разности
        for (j in 1 until n) {
            for (i in 0 until n - j) {
                differences[i][j] = differences[i + 1][j - 1] - differences[i][j - 1]
            }
        }

        // Преобразуем Array<DoubleArray> в List<List<Double>>
        val differencesList = differences.map { it.toList() }
        return FiniteDifferenceTable(points.map { it.x }, differencesList)
    }

    fun interpolate(points: List<DataPoint>, x0: Double, method: InterpolationMethod): Pair<Double, String> {
        return method.interpolate(points, x0)
    }

    fun isValidForFiniteDifferences(points: List<DataPoint>): Boolean {
        if (points.size < 2) return false
        
        val h = points[1].x - points[0].x
        for (i in 1 until points.size - 1) {
            val newH = points[i + 1].x - points[i].x
            if (abs(newH - h) > 1e-10) return false
        }
        return true
    }

    fun isValidForGaussOrStirling(points: List<DataPoint>): Boolean {
        if (points.size < 3) return false
        if (!isValidForFiniteDifferences(points)) return false
        return points.size % 2 == 1
    }

    fun isValidForBessel(points: List<DataPoint>): Boolean {
        if (points.size < 4) return false
        if (!isValidForFiniteDifferences(points)) return false
        return points.size % 2 == 0
    }

    fun setPoints(points: List<DataPoint>): FiniteDifferenceTable {
        return buildFiniteDifferenceTable(points)
    }
} 