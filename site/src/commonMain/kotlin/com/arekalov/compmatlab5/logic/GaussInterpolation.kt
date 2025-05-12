package com.arekalov.compmatlab5.logic

import com.arekalov.compmatlab5.models.DataPoint
import com.arekalov.compmatlab5.theme.ThemeColors
import com.varabyte.kobweb.compose.ui.graphics.Color
import kotlin.math.pow
import kotlin.math.round

// Метод Гаусса строит полином для равномерных узлов, используя центральные разности
// В зависимости от положения x0 относительно центра выбирает формулу "вперед" или "назад"
// Особенно эффективен для точек, близких к середине интервала
object GaussInterpolation : InterpolationMethod {
    private lateinit var themeColors: ThemeColors

    fun init(colors: ThemeColors) {
        themeColors = colors
    }

    override val color: Color
        get() = themeColors.primaryColor
    override val colorInverted: Color
        get() = themeColors.primaryInversedColor

    override val colorString: String
        get() = "#${themeColors.primaryString}"
    override val invertedColorString: String
        get() = "#${themeColors.primaryInversedString}"

    // Главный метод интерполяции Гаусса
    // 1. Строит таблицу конечных разностей
    // 2. Вычисляет t — относительное положение x0 относительно центра
    // 3. В зависимости от положения x0 выбирает формулу "вперед" или "назад"
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
        val dts1 = listOf(0, -1, 1, -2, 2, -3, 3, -4, 4) // Смещения для центральных разностей
        val t = (x0 - points[alphaInd].x) / h // t — относительное положение x0
        // В зависимости от положения x0 используем формулу вперед или назад
        val result = if (x0 > points[alphaInd].x) {
            // Формула Гаусса вперед
            calculateF1(points, finDifs, alphaInd, h, dts1, t)
        } else {
            // Формула Гаусса назад
            calculateF2(points, finDifs, alphaInd, h, dts1, t)
        }
        // Возвращаем значение и строку с полиномом
        return Pair(result, getPolynomial(points))
    }

    // Строит строку с аналитическим выражением полинома Гаусса
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
        val dts1 = listOf(0, -1, 1, -2, 2, -3, 3, -4, 4)
        val terms = mutableListOf<String>()
        // Первый член — значение функции в центре
        terms.add(formatNumber(points[alphaInd].y))
        // Остальные члены — разности с соответствующими множителями
        for (k in 1..n) {
            val term = buildString {
                append(" + ${formatNumber(finDifs[k][finDifs[k].size / 2] / InterpolationLogicController.factorial(k))}")
                for (j in 0 until k) {
                    append(" * ((x - ${formatNumber(points[alphaInd].x)})/${formatNumber(h)} + ${dts1[j]})")
                }
            }
            terms.add(term)
        }
        return "y = " + terms.joinToString("")
    }

    // Формула Гаусса вперед (используется в interpolate)
    // Считает сумму членов с положительными смещениями t + dts1[j]
    private fun calculateF1(
        points: List<DataPoint>,
        finDifs: List<List<Double>>,
        alphaInd: Int,
        h: Double,
        dts1: List<Int>,
        t: Double
    ): Double {
        var result = points[alphaInd].y
        // Каждый член — произведение множителей (t + dts1[j])
        for (k in 1..points.size - 1) {
            var mult = 1.0
            for (j in 0 until k) {
                mult *= (t + dts1[j])
            }
            result += mult * finDifs[k][finDifs[k].size / 2] / InterpolationLogicController.factorial(k)
        }
        return result
    }

    // Формула Гаусса назад (используется в interpolate)
    // Считает сумму членов с отрицательными смещениями t - dts1[j]
    private fun calculateF2(
        points: List<DataPoint>,
        finDifs: List<List<Double>>,
        alphaInd: Int,
        h: Double,
        dts1: List<Int>,
        t: Double
    ): Double {
        var result = points[alphaInd].y
        // Каждый член — произведение множителей (t - dts1[j])
        for (k in 1..points.size - 1) {
            var mult = 1.0
            for (j in 0 until k) {
                mult *= (t - dts1[j])
            }
            result += mult * finDifs[k][finDifs[k].size / 2 - (1 - finDifs[k].size % 2)] / InterpolationLogicController.factorial(k)
        }
        return result
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