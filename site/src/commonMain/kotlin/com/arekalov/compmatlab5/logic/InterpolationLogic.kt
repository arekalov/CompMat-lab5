package com.arekalov.compmatlab5.logic

import com.arekalov.compmatlab5.models.DataPoint
import com.arekalov.compmatlab5.models.FiniteDifferenceTable
import kotlin.math.pow
import kotlin.math.round

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

    fun getLagrangePolynomial(points: List<DataPoint>): String {
        val n = points.size
        val terms = mutableListOf<String>()
        
        for (i in 0 until n) {
            val term = buildString {
                append("${formatNumber(points[i].y)}")
                for (j in 0 until n) {
                    if (j != i) {
                        append(" * (x - ${formatNumber(points[j].x)}) / (${formatNumber(points[i].x)} - ${formatNumber(points[j].x)})")
                    }
                }
            }
            terms.add(term)
        }
        
        return terms.joinToString(" + ")
    }

    fun getNewtonPolynomial(points: List<DataPoint>): String {
        val table = buildFiniteDifferenceTable(points)
        val n = points.size
        val terms = mutableListOf<String>()
        
        // Первый член - y[0]
        terms.add(formatNumber(table.differences[0][0]))
        
        // Остальные члены
        for (i in 1 until n) {
            val term = buildString {
                append(" + ${formatNumber(table.differences[0][i] / factorial(i))}")
                for (j in 0 until i) {
                    append(" * (x - ${formatNumber(points[j].x)})")
                }
            }
            terms.add(term)
        }
        
        return terms.joinToString("")
    }

    fun getT(points: List<DataPoint>, x0: Double): Double {
        if (points.size < 2) return 0.0
        val h = points[1].x - points[0].x
        return (x0 - points[0].x) / h
    }

    fun formatNumber(number: Double): String {
        return if (kotlin.math.abs(number) < 1e-10) "0" else formatNumber(number, 2)
    }

    private fun formatNumber(number: Double, precision: Int): String {
        val factor = 10.0.pow(precision)
        val roundedNumber = round(number * factor) / factor
        return roundedNumber.toString()
    }

} 