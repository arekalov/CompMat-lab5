package com.arekalov.compmatlab5.logic

import com.arekalov.compmatlab5.models.DataPoint
import com.arekalov.compmatlab5.theme.ThemeColors
import com.varabyte.kobweb.compose.ui.graphics.Color
import kotlin.math.pow
import kotlin.math.round

// Метод Бесселя — модификация Гаусса для четного числа равномерных узлов, когда x0 между двумя центральными точками
// Использует средние значения и специальные формулы для центральных разностей
object BesselInterpolation : InterpolationMethod {
    private lateinit var themeColors: ThemeColors

    fun init(colors: ThemeColors) {
        themeColors = colors
    }

    override val colorString: String
        get() = "#${themeColors.warningString}"
    override val invertedColorString: String
        get() = "#${themeColors.warningInversedString}"

    override val color: Color
        get() = themeColors.warningColor
    override val colorInverted: Color
        get() = themeColors.warningInversedColor

    // Главный метод интерполяции Бесселя
    // 1. Строит таблицу конечных разностей
    // 2. Вычисляет t — относительное положение x0 относительно центра
    // 3. Начальное значение — среднее двух центральных точек
    // 4. Добавляет слагаемые по формуле Бесселя
    override fun interpolate(points: List<DataPoint>, x0: Double): Pair<Double, String> {
        val n = points.size - 1
        val alphaInd = n / 2
        // Строим таблицу конечных разностей (каждый следующий столбец — разности предыдущего)
        val finDifs = mutableListOf<List<Double>>()
        finDifs.add(points.map { it.y })
        for (k in 1..n) {
            val last = finDifs.last()
            // Каждый элемент — разность двух соседних из предыдущего столбца
            finDifs.add((0 until n - k + 1).map { i -> last[i + 1] - last[i] })
        }
        val h = points[1].x - points[0].x
        val dts1 = listOf(0, -1, 1, -2, 2, -3, 3, -4, 4, -5, 5) // Смещения для центральных разностей
        val t = (x0 - points[alphaInd].x) / h // t — относительное положение x0
        // Начальное значение — среднее двух центральных точек
        var result = (points[alphaInd].y + points[alphaInd + 1].y) / 2
        // Добавляем слагаемые по формуле Бесселя
        for (k in 1..n) {
            var mult1 = 1.0
            var mult2 = 1.0
            for (j in 0 until k) {
                if (j < dts1.size) {
                    mult1 *= (t + dts1[j])
                    mult2 *= (t + dts1[j])
                }
            }
            if (k < finDifs.size && finDifs[k].size > finDifs[k].size / 2) {
                result += mult1 * finDifs[k][finDifs[k].size / 2] / InterpolationLogicController.factorial(2 * k) +
                        (t - 0.5) * mult2 * finDifs[k][finDifs[k].size / 2] / InterpolationLogicController.factorial(2 * k + 1)
            }
        }
        // Возвращаем значение и строку с полиномом
        return Pair(result, getPolynomial(points))
    }

    // Строит строку с аналитическим выражением полинома Бесселя
    // Использует те же центральные разности, что и interpolate
    override fun getPolynomial(points: List<DataPoint>): String {
        val n = points.size - 1
        val alphaInd = n / 2
        val finDifs = mutableListOf<List<Double>>()
        finDifs.add(points.map { it.y })
        for (k in 1..n) {
            val last = finDifs.last()
            finDifs.add((0 until n - k + 1).map { i -> last[i + 1] - last[i] })
        }
        val h = points[1].x - points[0].x
        val dts1 = listOf(0, -1, 1, -2, 2, -3, 3, -4, 4, -5, 5)
        val terms = mutableListOf<String>()
        // Первый член — среднее двух центральных точек
        terms.add("(${formatNumber(points[alphaInd].y)} + ${formatNumber(points[alphaInd + 1].y)}) / 2")
        // Остальные члены — разности с соответствующими множителями
        for (k in 1..n) {
            if (k < finDifs.size && finDifs[k].size > finDifs[k].size / 2) {
                val term = buildString {
                    append(" + ${formatNumber(finDifs[k][finDifs[k].size / 2] / InterpolationLogicController.factorial(2 * k))}")
                    for (j in 0 until k) {
                        if (j < dts1.size) {
                            append(" * ((x - ${formatNumber(points[alphaInd].x)})/${formatNumber(h)} + ${dts1[j]})")
                        }
                    }
                    append(" + ((x - ${formatNumber(points[alphaInd].x)})/${formatNumber(h)} - 0.5) * ${formatNumber(finDifs[k][finDifs[k].size / 2] / InterpolationLogicController.factorial(2 * k + 1))}")
                    for (j in 0 until k) {
                        if (j < dts1.size) {
                            append(" * ((x - ${formatNumber(points[alphaInd].x)})/${formatNumber(h)} + ${dts1[j]})")
                        }
                    }
                }
                terms.add(term)
            }
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