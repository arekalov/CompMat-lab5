package com.arekalov.compmatlab5.logic

import com.arekalov.compmatlab5.models.DataPoint
import com.arekalov.compmatlab5.models.FiniteDifferenceTable
import com.arekalov.compmatlab5.models.InterpolationMethod
import com.arekalov.compmatlab5.models.InterpolationResult

object InterpolationMethodsBase {
    internal fun factorial(n: Int): Int {
        return if (n <= 1) 1 else n * factorial(n - 1)
    }

    internal fun getStep(points: List<DataPoint>): Double {
        return points[1].x - points[0].x
    }

    internal fun finiteDifferences(points: List<DataPoint>): List<List<Double>> {
        val n = points.size
        val finDifs = mutableListOf<List<Double>>()
        finDifs.add(points.map { it.y })
        for (k in 1 until n) {
            val last = finDifs.last()
            finDifs.add((0 until n - k).map { i ->
                last[i + 1] - last[i]
            })
        }
        return finDifs
    }

    internal fun dividedDifferences(points: List<DataPoint>): List<Double> {
        val n = points.size
        val coef = points.map { it.y }.toMutableList()

        for (j in 1 until n) {
            for (i in n - 1 downTo j) {
                coef[i] = (coef[i] - coef[i - 1]) / (points[i].x - points[i - j].x)
            }
        }

        return coef
    }

    private fun interpolate(points: List<DataPoint>, x0: Double, method: InterpolationMethod): Double {
        return when (method) {
            InterpolationMethod.LagrangeInterpolation -> lagrangeInterpolate(points, x0)
            InterpolationMethod.NewtonDividedDifferenceInterpolation -> newtonDividedDifferenceInterpolate(points, x0)
            InterpolationMethod.GaussInterpolation -> gaussInterpolate(points, x0)
            InterpolationMethod.NewtonFiniteDifferenceInterpolation -> newtonFiniteDifferenceInterpolate(points, x0)
        }
    }

    fun getResult(
        points: List<DataPoint>,
        x0: Double,
        a: Double,
        b: Double,
        method: InterpolationMethod,
        dx: Double = 0.1
    ): InterpolationResult {
        val result = mutableListOf<DataPoint>()
        val value = interpolate(points, x0, method)
        var x = a
        while (x <= b) {
            result.add(DataPoint(x = x, y = interpolate(points, x, method)))
            x += dx
        }
        points.forEach { result.add(it) }
        result.add(DataPoint(x = x0, y = interpolate(points, x0, method)))
        result.add(DataPoint(x = b, y = interpolate(points, b, method)))

        return InterpolationResult(
            points = result.sortedBy { it.x },
            value = value,
            method = method,
        )
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
} 