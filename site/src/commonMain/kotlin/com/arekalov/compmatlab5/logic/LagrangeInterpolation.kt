package com.arekalov.compmatlab5.logic

import com.arekalov.compmatlab5.models.DataPoint
import com.arekalov.compmatlab5.theme.ThemeColors
import com.varabyte.kobweb.compose.ui.graphics.Color
import kotlin.math.pow
import kotlin.math.round

// Метод Лагранжа строит полином, проходящий через все точки, как сумму базисных полиномов
// Для каждой точки строится свой базис, равный 1 в этой точке и 0 в остальных
// Итоговый полином — сумма этих базисов, умноженных на значения функции в точках
object LagrangeInterpolation : InterpolationMethod {
    private lateinit var themeColors: ThemeColors

    fun init(colors: ThemeColors) {
        themeColors = colors
    }

    override val colorString: String
        get() = "#${themeColors.errorString}"
    override val invertedColorString: String
        get() = "#${themeColors.errorIversedString}"

    override val color: Color
        get() = themeColors.errorColor
    override val colorInverted: Color
        get() = themeColors.errorInversedColor

    // Главный метод интерполяции Лагранжа
    // 1. Для каждой точки строит базисный полином
    // 2. Перемножает (x0 - x_j)/(x_i - x_j) по всем j != i
    // 3. Складывает все базисы с весами y_i
    override fun interpolate(points: List<DataPoint>, x0: Double): Pair<Double, String> {
        val n = points.size
        var result = 0.0
        // Для каждой точки строим базисный полином
        for (i in 0 until n) {
            var term = points[i].y
            // Перемножаем (x0 - x_j)/(x_i - x_j) по всем j != i
            for (j in 0 until n) {
                if (i != j) {
                    term *= (x0 - points[j].x) / (points[i].x - points[j].x)
                }
            }
            // Прибавляем вклад текущей точки
            result += term
        }
        // Возвращаем значение и строку с полиномом
        return Pair(result, getPolynomial(points))
    }

    // Строит строку с аналитическим выражением полинома Лагранжа
    // Для каждого i строит произведение по всем j != i
    override fun getPolynomial(points: List<DataPoint>): String {
        val n = points.size
        val terms = mutableListOf<String>()
        for (i in 0 until n) {
            val term = buildString {
                append("${formatNumber(points[i].y)}")
                for (j in 0 until n) {
                    if (i != j) {
                        append(" * (x - ${formatNumber(points[j].x)}) / (${formatNumber(points[i].x)} - ${formatNumber(points[j].x)})")
                    }
                }
            }
            terms.add(term)
        }
        return "y = " + terms.joinToString(" + ")
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