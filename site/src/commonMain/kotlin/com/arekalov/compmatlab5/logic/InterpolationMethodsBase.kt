package com.arekalov.compmatlab5.logic

import com.arekalov.compmatlab5.models.DataPoint
import com.arekalov.compmatlab5.models.FiniteDifferenceTable
import com.arekalov.compmatlab5.models.InterpolationMethod
import com.arekalov.compmatlab5.models.InterpolationResult

object InterpolationMethodsBase {
    // Рекурсивная функция для вычисления факториала числа n
    internal fun factorial(n: Int): Int {
        return if (n <= 1) 1 else n * factorial(n - 1)
    }

    // Строит таблицу конечных разностей для списка точек
    // Возвращает список списков, где каждый следующий список — разности следующего порядка
    internal fun finiteDifferences(points: List<DataPoint>): List<List<Double>> {
        val n = points.size
        val finDifs = mutableListOf<List<Double>>()
        // Первый ряд — значения y
        finDifs.add(points.map { it.y })
        // Каждый следующий ряд — разности предыдущего ряда
        for (k in 1 until n) {
            val last = finDifs.last()
            finDifs.add((0 until n - k).map { i ->
                last[i + 1] - last[i]
            })
        }
        return finDifs
    }

    // Вычисляет разделённые разности для списка точек (используется в методе Ньютона)
    // Возвращает список коэффициентов для полинома Ньютона
    internal fun dividedDifferences(points: List<DataPoint>): List<Double> {
        val n = points.size
        val coef = points.map { it.y }.toMutableList()

        // Основной цикл по порядку разности
        for (j in 1 until n) {
            // Обратный проход по массиву коэффициентов
            for (i in n - 1 downTo j) {
                coef[i] = (coef[i] - coef[i - 1]) / (points[i].x - points[i - j].x)
            }
        }

        return coef
    }

    // Универсальный метод для вызова нужной интерполяционной формулы по типу
    // Используется внутри getResult и для вычисления значения в точке
    private fun interpolate(points: List<DataPoint>, x0: Double, method: InterpolationMethod): Double {
        return when (method) {
            InterpolationMethod.LagrangeInterpolation -> lagrangeInterpolate(points, x0)
            InterpolationMethod.NewtonDividedDifferenceInterpolation -> newtonDividedDifferenceInterpolate(points, x0)
            InterpolationMethod.GaussInterpolation -> gaussInterpolate(points, x0)
        }
    }

    // Строит результат интерполяции: значение в x0 и набор точек для построения графика полинома
    // dx — шаг для построения графика
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
        // Строим значения полинома на всём интервале [a, b] с шагом dx
        while (x <= b) {
            result.add(DataPoint(x = x, y = interpolate(points, x, method)))
            x += dx
        }
        // Добавляем исходные точки и значения в x0 и b для точности
        points.forEach { result.add(it) }
        result.add(DataPoint(x = x0, y = interpolate(points, x0, method)))
        result.add(DataPoint(x = b, y = interpolate(points, b, method)))

        return InterpolationResult(
            points = result.sortedBy { it.x },
            value = value,
            method = method,
        )
    }

    // Строит таблицу конечных разностей для отображения (двумерный массив)
    // Используется для визуализации в интерфейсе
    fun buildFiniteDifferenceTable(points: List<DataPoint>): FiniteDifferenceTable {
        val n = points.size
        val differences = Array(n) { DoubleArray(n) { 0.0 } }

        // Первый столбец — значения y
        for (i in 0 until n) {
            differences[i][0] = points[i].y
        }

        // Остальные столбцы — конечные разности
        for (j in 1 until n) {
            for (i in 0 until n - j) {
                differences[i][j] = differences[i + 1][j - 1] - differences[i][j - 1]
            }
        }

        // Преобразуем в список для передачи в UI
        val differencesList = differences.map { it.toList() }
        return FiniteDifferenceTable(points.map { it.x }, differencesList)
    }
} 