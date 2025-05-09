package com.arekalov.compmatlab5.models

/**
 * Таблица конечных разностей для вывода в UI и вычислений.
 * differences[i][j] — конечная разность порядка j для точки i
 */
data class FiniteDifferenceTable(
    val x: List<Double>,
    val differences: List<List<Double>> // differences[i][j] — разность порядка j для x[i]
) 