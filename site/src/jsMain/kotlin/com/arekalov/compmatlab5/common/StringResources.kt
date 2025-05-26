package com.arekalov.compmatlab5.common

object StringResources {
    // Footer
    const val MADE_BY_STR = "Made by arekalov"
    const val SOURCE_CODE_STR = "Source code here"
    const val CHANGE_THEME_STR = "Change theme"

    // Graph Section
    const val GRAPH_TITLE = "График"
    const val NO_DATA_MESSAGE = "Нет данных для отображения"

    // Interpolation messages
    const val GAUSS_POINTS_NOT_EQUIDISTANT = "Точки не равноотстоящие или их меньше 3 для метода Гаусса"
    const val GAUSS_EXTRAPOLATION_WARNING = "Внимание: x0 вне диапазона точек, используется экстраполяция методом Гаусса"
    const val GAUSS_POINTS_LOG = "Шаги между точками: "

    // ResultSection
    const val RESULTS_LABEL = "Результаты"
    const val ERROR_PREFIX = "Ошибка: "
    const val GENERATED_POINTS_LABEL = "Сгенерированные точки:"
    const val FINITE_DIFF_TABLE_LABEL = "Таблица конечных разностей:"
    const val X_LABEL = "x"
    const val DELTA_LABEL_PREFIX = "Δ"
    const val VALUE_AT_X0_LABEL = "Значение в x₀: "

    // DataInputSection
    const val DATA_INPUT_LABEL = "Ввод данных"
    const val MANUAL_FORMAT_HINT = "Формат: x y (по одной паре на строку)"
    const val SET_POINTS_BUTTON = "Задать точки"
    const val FILE_FORMAT_HINT = "Выберите текстовый файл с точками в формате: x y (по одной паре на строку)"
    const val FUNCTION_HINT = "Выберите функцию и параметры"
    const val INTERVAL_LABEL = "Интервал [a, b]:"
    const val NUM_POINTS_LABEL = "Количество точек:"
    const val GENERATE_POINTS_BUTTON = "Сгенерировать точки"
    const val X0_LABEL = "Значение x₀ для интерполяции:"
    const val CALCULATE_BUTTON = "Вычислить"
}

