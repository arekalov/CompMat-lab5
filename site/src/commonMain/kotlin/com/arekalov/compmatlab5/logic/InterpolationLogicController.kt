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
        val step = points[1].x - points[0].x
        val isValid = points.zipWithNext().all { (p1, p2) ->
            abs(p2.x - p1.x - step) < 1e-10
        }
        println("Newton validation: $isValid (step: $step)")
        return isValid
    }

    fun isValidForGaussOrStirling(points: List<DataPoint>): Boolean {
        if (points.size < 3 || points.size % 2 == 0) return false
        val step = points[1].x - points[0].x
        val isValid = points.zipWithNext().all { (p1, p2) ->
            abs(p2.x - p1.x - step) < 1e-10
        }
        println("Gauss/Stirling validation: $isValid (step: $step, size: ${points.size})")
        return isValid
    }

    fun isValidForBessel(points: List<DataPoint>): Boolean {
        if (points.size < 4 || points.size % 2 != 0) return false
        val step = points[1].x - points[0].x
        val isValid = points.zipWithNext().all { (p1, p2) ->
            abs(p2.x - p1.x - step) < 1e-10
        }
        println("Bessel validation: $isValid (step: $step, size: ${points.size})")
        return isValid
    }

    fun setPoints(points: List<DataPoint>): FiniteDifferenceTable {
        return buildFiniteDifferenceTable(points)
    }
} 