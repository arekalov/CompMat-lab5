package com.arekalov.compmatlab5.logic

import com.arekalov.compmatlab5.models.DataPoint
import com.arekalov.compmatlab5.theme.ThemeColors
import com.varabyte.kobweb.compose.ui.graphics.Color
import kotlin.math.pow
import kotlin.math.round

// Метод Ньютона строит полином с помощью таблицы конечных разностей
// Каждый следующий член учитывает разности между значениями функции и расстояния до узлов
// Удобен для добавления новых точек и равномерных сеток
object NewtonInterpolation : InterpolationMethod {
    private lateinit var themeColors: ThemeColors

    fun init(colors: ThemeColors) {
        themeColors = colors
    }

    override val colorString: String
        get() = "#${themeColors.successString}"
    override val invertedColorString: String
        get() = "#${themeColors.successSInversedtSring}"

    override val color: Color
        get() = themeColors.successColor
    override val colorInverted: Color
        get() = themeColors.successInversedColor

    // Главный метод интерполяции Ньютона
    // 1. Строит таблицу конечных разностей
    // 2. Вычисляет t — относительное положение x0 относительно первого узла
    // 3. Добавляет члены полинома Ньютона
    override fun interpolate(points: List<DataPoint>, x0: Double): Pair<Double, String> {
        val n = points.size
        // Строим таблицу конечных разностей
        val table = InterpolationLogicController.buildFiniteDifferenceTable(points)
        val h = points[1].x - points[0].x
        val t = (x0 - points[0].x) / h
        var result = table.differences[0][0]
        var mult = 1.0
        // Добавляем члены полинома Ньютона
        for (i in 1 until n) {
            mult *= (t - (i - 1))
            result += table.differences[0][i] / InterpolationLogicController.factorial(i) * mult
        }
        // Возвращаем значение и строку с полиномом
        return Pair(result, getPolynomial(points))
    }

    // Строит строку с аналитическим выражением полинома Ньютона
    // Для каждого i строит произведение (x - x_j) по всем j < i
    override fun getPolynomial(points: List<DataPoint>): String {
        val n = points.size
        val table = InterpolationLogicController.buildFiniteDifferenceTable(points)
        val h = points[1].x - points[0].x
        val terms = mutableListOf<String>()
        // Первый член — значение функции в первом узле
        terms.add(formatNumber(table.differences[0][0]))
        // Остальные члены — разности с соответствующими множителями
        for (i in 1 until n) {
            val term = buildString {
                append(" + ${formatNumber(table.differences[0][i] / InterpolationLogicController.factorial(i))}")
                for (j in 0 until i) {
                    append(" * (x - ${formatNumber(points[j].x)})")
                }
            }
            terms.add(term)
        }
        return "y = " + terms.joinToString("")
    }

    // Форматирует число для вывода (2 знака после запятой)
    private fun formatNumber(number: Double): String {
        return if (kotlin.math.abs(number) < 1e-10) "0" else formatNumber(number, 2)
    }

    // Округляет число до precision знаков после запятой
    private fun formatNumber(number: Double, precision: Int): String {
        val factor = 10.0.pow(precision)
        val roundedNumber = round(number * factor) / factor
        return roundedNumber.toString()
    }
}