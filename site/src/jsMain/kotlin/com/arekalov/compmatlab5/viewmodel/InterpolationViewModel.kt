package com.arekalov.compmatlab5.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arekalov.compmatlab5.components.widgets.AppColors
import com.arekalov.compmatlab5.data.GraphManager
import com.arekalov.compmatlab5.logic.*
import com.arekalov.compmatlab5.models.*
import com.arekalov.compmatlab5.theme.getColorString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.*

class InterpolationViewModel {
    private val graphManager = GraphManager()

    init {
        graphManager.initGraph()
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
                finiteDifferenceTable = InterpolationMethodsBase.buildFiniteDifferenceTable(points)
            )
        }
        graphManager.clearGraph()
        graphManager.plotPoints(id = "predv", points, "#0C24A4")
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
            val a = points.firstOrNull { it.x == points.minOf { it.x } }?.x ?: 0.0
            val b =  points.firstOrNull { it.x == points.maxOf { it.x } }?.x ?: 1.0

            // Лагранж всегда доступен
            val lagrangeResult = InterpolationMethodsBase.getResult(
                points = points,
                x0 = x0,
                method = InterpolationMethod.LagrangeInterpolation,
                a = a,
                b = b,
            )
            results.add(lagrangeResult)

//            val newtonResult = InterpolationMethodsBase.getResult(
//                points = points,
//                x0 = x0,
//                method = InterpolationMethod.NewtonFiniteDifferenceInterpolation,
//                a = a,
//                b = b,
//            )
//            results.add(newtonResult)

            val newtonResult2 = InterpolationMethodsBase.getResult(
                points = points,
                x0 = x0,
                method = InterpolationMethod.NewtonDividedDifferenceInterpolation,
                a = a,
                b = b,
            )
            results.add(newtonResult2)

            val gauss = InterpolationMethodsBase.getResult(
                points = points,
                x0 = x0,
                method = InterpolationMethod.GaussInterpolation,
                a = a,
                b = b,
            )
            results.add(gauss)

//            val stirling = InterpolationMethodsBase.getResult(
//                points = points,
//                x0 = x0,
//                method = InterpolationMethod.StirlingInterpolation,
//                a = a,
//                b = b,
//            )
//            results.add(stirling)

//            val bessel = InterpolationMethodsBase.getResult(
//                points = points,
//                x0 = x0,
//                method = InterpolationMethod.BesselInterpolation,
//                a = a,
//                b = b,
//            )
//            results.add(bessel)

            _state.update {
                it.copy(
                    interpolationResults = results,
                    error = null
                )
            }

            // Отображаем все полиномы на графике
            results.forEach { result ->
                graphManager.plotPoints(
                    id = result.method.name,
                    points = result.points,
                    isLinesEnabled = true,
                    colorValue = result.method.getColorString(isDarkTheme),
                )
                graphManager.plotPoints(
                    id = result.method.name + "value",
                    points = listOf(DataPoint(x0, result.value)),
                    colorValue = result.method.getColorString(isDarkTheme),
                    isLinesEnabled = false
                )
            }

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
}

data class InterpolationState(
    val points: List<DataPoint> = emptyList(),
    val x0: Double? = null,
    val finiteDifferenceTable: FiniteDifferenceTable? = null,
    val interpolationResults: List<InterpolationResult> = emptyList(),
    val error: String? = null
)

