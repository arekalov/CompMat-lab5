package com.arekalov.compmatlab5.logic

import com.arekalov.compmatlab5.models.DataPoint

// Вычисляет значение интерполяционного полинома Ньютона (разделённые разности) в точке x0
// points — список исходных точек (x, y)
fun newtonDividedDifferenceInterpolate(points: List<DataPoint>, x0: Double): Double {
    // Получаем коэффициенты разделённых разностей
    val coef = InterpolationMethodsBase.dividedDifferences(points)
    var result = points[0].y
    
    // Строим полином по схеме Горнера
    for (k in 1 until points.size) {
        var term = coef[k]
        // Перемножаем на (x0 - x_j) для всех предыдущих x_j
        for (j in 0 until k) {
            term *= (x0 - points[j].x)
        }
        result += term
    }
    
    return result
}
