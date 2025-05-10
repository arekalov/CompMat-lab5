package com.arekalov.compmatlab5.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arekalov.compmatlab5.components.widgets.AppColors
import com.arekalov.compmatlab5.data.GraphManager
import com.arekalov.compmatlab5.models.*
import com.arekalov.compmatlab5.logic.InterpolationLogicController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.*

class InterpolationViewModel {
    private val graphManager = GraphManager()

    init {
        graphManager.initGraph()
    }

    var isDarkTheme by mutableStateOf(true)

    // Состояния для UI
    private val _points = MutableStateFlow<List<DataPoint>>(emptyList())
    val points: StateFlow<List<DataPoint>> = _points

    private val _x0 = MutableStateFlow<Double?>(null)
    val x0: StateFlow<Double?> = _x0

    private val _result = MutableStateFlow<InterpolationResult?>(null)
    val result: StateFlow<InterpolationResult?> = _result

    private val _finiteDifferenceTable = MutableStateFlow<FiniteDifferenceTable?>(null)
    val finiteDifferenceTable: StateFlow<FiniteDifferenceTable?> = _finiteDifferenceTable

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Новый функционал:
    private val _inputType = MutableStateFlow<InputType>(InputType.MANUAL)
    val inputType: StateFlow<InputType> = _inputType

    private val _functionType = MutableStateFlow<FunctionType>(FunctionType.SIN)
    val functionType: StateFlow<FunctionType> = _functionType

    fun setInputType(type: InputType) {
        _inputType.value = type
    }

    fun setFunctionType(type: FunctionType) {
        _functionType.value = type
    }

    // Генерация точек по выбранной функции
    fun generatePointsByFunction(a: Double, b: Double, n: Int) {
        val func = when (_functionType.value) {
            FunctionType.SIN -> { x: Double -> sin(x) }
            FunctionType.COS -> { x: Double -> cos(x) }
            FunctionType.EXP -> { x: Double -> exp(x) }
        }
        val step = if (n > 1) (b - a) / (n - 1) else 0.0
        val points = List(n) { i ->
            val x = a + i * step
            DataPoint(x, func(x))
        }
        setPoints(points)
    }

    // Методы для UI
    fun setPoints(newPoints: List<DataPoint>) {
        _points.value = newPoints
        recalculateTable()
        graphManager.clearGraph()
        graphManager.plotPoints(newPoints)
    }

    fun setX0(newX0: Double?) {
        _x0.value = newX0
    }

    fun calculate() {
        val points = _points.value
        val x0 = _x0.value

        if (points.isEmpty() || x0 == null) {
            _error.value = "Введите точки и значение x0"
            return
        }

        try {
            val lagrangeValue = InterpolationLogicController.lagrange(points, x0)
            val newtonValue = InterpolationLogicController.newton(points, x0)

            val tValue = InterpolationLogicController.getT(points, x0)

            _result.value = InterpolationResult(
                lagrangeValue = lagrangeValue,
                newtonValue = newtonValue,
                tValue = tValue
            )
            _error.value = null

            // Отображаем многочлены на графике
            val lagrangePolynomial = InterpolationLogicController.getLagrangePolynomial(points)
            val newtonPolynomial = InterpolationLogicController.getNewtonPolynomial(points)
            
            // Многочлен Лагранжа красным цветом
            graphManager.plotFunction(
                lagrangePolynomial, if (isDarkTheme) {
                    "#${AppColors.errorIversedString}"
                } else "#${AppColors.errorString}", false
            )
            // Многочлен Ньютона зеленым цветом
            graphManager.plotFunction(
                newtonPolynomial, if (isDarkTheme) {
                    "#${AppColors.successSInversedtSring}"
                } else "#${AppColors.successString}", false
            )

            // Отображаем точку x₀ и значения y
            val lagrangePoint = "(${x0}, ${lagrangeValue})"
            val newtonPoint = "(${x0}, ${newtonValue})"
            
            // Точка для метода Лагранжа
            graphManager.plotFunction(
                lagrangePoint, if (isDarkTheme) {
                    "#${AppColors.errorIversedString}"
                } else "#${AppColors.errorString}", false
            )
            // Точка для метода Ньютона
            graphManager.plotFunction(
                newtonPoint, if (isDarkTheme) {
                    "#${AppColors.successSInversedtSring}"
                } else "#${AppColors.successString}", false
            )
        } catch (e: Exception) {
            _error.value = "Ошибка вычисления: ${e.message}"
        }
    }

    fun clearState() {
        _points.value = emptyList()
        _result.value = null
        _finiteDifferenceTable.value = null
        graphManager.clearGraph()
    }

    private fun recalculateTable() {
        val points = _points.value
        if (points.isNotEmpty()) {
            _finiteDifferenceTable.value = InterpolationLogicController.buildFiniteDifferenceTable(points)
        } else {
            _finiteDifferenceTable.value = null
        }
    }

    fun setTheme(isDark: Boolean) {
        isDarkTheme = isDark
        graphManager.setTheme(isDark)
    }
} 