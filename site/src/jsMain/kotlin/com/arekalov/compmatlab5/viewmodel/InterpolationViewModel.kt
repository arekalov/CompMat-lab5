package com.arekalov.compmatlab5.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arekalov.compmatlab5.components.widgets.AppColors
import com.arekalov.compmatlab5.data.GraphManager
import com.arekalov.compmatlab5.logic.*
import com.arekalov.compmatlab5.models.*
import com.arekalov.compmatlab5.theme.JsThemeColors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.*
import com.varabyte.kobweb.compose.ui.graphics.Color

class InterpolationViewModel {
    private val graphManager = GraphManager()

    init {
        graphManager.initGraph()
        // Инициализируем цвета для всех методов интерполяции
        LagrangeInterpolation.init(JsThemeColors)
        NewtonInterpolation.init(JsThemeColors)
        GaussInterpolation.init(JsThemeColors)
        StirlingInterpolation.init(JsThemeColors)
        BesselInterpolation.init(JsThemeColors)
    }

    var isDarkTheme by mutableStateOf(true)

    private val _state = MutableStateFlow(InterpolationState())
    val state: StateFlow<InterpolationState> = _state.asStateFlow()

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
        updatePoints(points)
    }

    fun updatePoints(points: List<DataPoint>) {
        _state.update { currentState ->
            currentState.copy(
                points = points,
                finiteDifferenceTable = InterpolationLogicController.buildFiniteDifferenceTable(points)
            )
        }
        graphManager.clearGraph()
        graphManager.plotPoints(points)
    }

    fun updateX0(x0: Double) {
        _state.update { it.copy(x0 = x0) }
    }

    fun calculate() {
        val currentState = _state.value
        val points = currentState.points
        val x0 = currentState.x0

        if (points.isEmpty() || x0 == null) {
            _state.update { it.copy(error = "Введите точки и значение x0") }
            return
        }

        try {
            val results = mutableListOf<InterpolationResult>()

            // Лагранж всегда доступен
            val lagrangeResult = InterpolationLogicController.interpolate(points, x0, LagrangeInterpolation)
            results.add(InterpolationResult(
                methodName = "Многочлен Лагранжа",
                value = lagrangeResult.first,
                polynomial = lagrangeResult.second,
                colorString = LagrangeInterpolation.colorString,
                invertedColorString = LagrangeInterpolation.invertedColorString,
                color = LagrangeInterpolation.color,
                invertedColor = LagrangeInterpolation.colorInverted
            ))

            // Ньютон с конечными разностями
            if (InterpolationLogicController.isValidForFiniteDifferences(points)) {
                val newtonResult = InterpolationLogicController.interpolate(points, x0, NewtonInterpolation)
                results.add(InterpolationResult(
                    methodName = "Многочлен Ньютона",
                    value = newtonResult.first,
                    polynomial = newtonResult.second,
                    colorString = NewtonInterpolation.colorString,
                    invertedColorString = NewtonInterpolation.invertedColorString,
                    color = NewtonInterpolation.color,
                    invertedColor = NewtonInterpolation.colorInverted
                ))
            }

            // Гаусс
            if (InterpolationLogicController.isValidForGaussOrStirling(points)) {
                val gaussResult = InterpolationLogicController.interpolate(points, x0, GaussInterpolation)
                results.add(InterpolationResult(
                    methodName = "Многочлен Гаусса",
                    value = gaussResult.first,
                    polynomial = gaussResult.second,
                    colorString = GaussInterpolation.colorString,
                    invertedColorString = GaussInterpolation.invertedColorString,
                    color = GaussInterpolation.color,
                    invertedColor = GaussInterpolation.colorInverted
                ))
            }

            // Стирлинг
            if (InterpolationLogicController.isValidForGaussOrStirling(points)) {
                val stirlingResult = InterpolationLogicController.interpolate(points, x0, StirlingInterpolation)
                results.add(InterpolationResult(
                    methodName = "Многочлен Стирлинга",
                    value = stirlingResult.first,
                    polynomial = stirlingResult.second,
                    colorString = StirlingInterpolation.colorString,
                    invertedColorString = StirlingInterpolation.invertedColorString,
                    color = StirlingInterpolation.color,
                    invertedColor = StirlingInterpolation.colorInverted
                ))
            }

            // Бессель
            if (InterpolationLogicController.isValidForBessel(points)) {
                val besselResult = InterpolationLogicController.interpolate(points, x0, BesselInterpolation)
                results.add(InterpolationResult(
                    methodName = "Многочлен Бесселя",
                    value = besselResult.first,
                    polynomial = besselResult.second,
                    colorString = BesselInterpolation.colorString,
                    invertedColorString = BesselInterpolation.invertedColorString,
                    color = BesselInterpolation.color,
                    invertedColor = BesselInterpolation.colorInverted
                ))
            }

            _state.update { it.copy(
                interpolationResults = results,
                error = null
            ) }

            // Отображаем все полиномы на графике
            results.forEach { result ->
                graphManager.plotFunction(result.polynomial, getCurrentStringColor(color = result.colorString, inverted = result.invertedColorString), false)
            }

            // Отображаем точку x₀ и значения y для каждого метода
//            results.forEach { result ->
//                val point = "(${x0}, ${result.value})"
//                graphManager.plotFunction(point, getCurrentStringColor(color = result.colorString, inverted = result.invertedColorString), false)
//            }

        } catch (e: Exception) {
            _state.update { it.copy(error = "Ошибка вычисления: ${e.message}") }
        }
    }

    fun clearState() {
        _state.update { InterpolationState() }
        graphManager.clearGraph()
    }

    fun setTheme(isDark: Boolean) {
        isDarkTheme = isDark
        graphManager.setTheme(isDark)
    }

    fun getCurrentStringColor(color: String, inverted: String) = if (isDarkTheme) inverted else color
    fun getCurrentColor(color: Color, inverted: Color) = if (!isDarkTheme) inverted else color


}

data class InterpolationState(
    val points: List<DataPoint> = emptyList(),
    val x0: Double? = null,
    val finiteDifferenceTable: FiniteDifferenceTable? = null,
    val interpolationResults: List<InterpolationResult> = emptyList(),
    val error: String? = null
)

