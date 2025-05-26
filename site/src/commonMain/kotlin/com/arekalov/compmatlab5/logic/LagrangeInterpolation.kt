package com.arekalov.compmatlab5.logic

import com.arekalov.compmatlab5.models.DataPoint

// Вычисляет значение интерполяционного полинома Лагранжа в точке x0
// points — список исходных точек (x, y)
fun lagrangeInterpolate(points: List<DataPoint>, x0: Double): Double {
    val n = points.size
    var result = 0.0
    
    // Для каждого узла строим базисный полином Лагранжа
    for (i in 0 until n) {
        var term = points[i].y
        for (j in 0 until n) {
            if (i != j) {
                // Множитель для базисного полинома: (x0 - x_j) / (x_i - x_j)
                term *= (x0 - points[j].x) / (points[i].x - points[j].x)
            }
        }
        result += term // Суммируем вклад каждого базисного полинома
    }
    
    return result
}
