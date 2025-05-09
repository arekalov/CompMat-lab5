package com.arekalov.compmatlab5.components.sections

import androidx.compose.runtime.*
import com.arekalov.compmatlab5.models.*
import com.arekalov.compmatlab5.viewmodel.InterpolationViewModel
import com.arekalov.compmatlab5.components.widgets.*
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.width
import org.jetbrains.compose.web.css.cssRem

@Composable
fun DataInputSection(viewModel: InterpolationViewModel) {
    var inputText by remember { mutableStateOf("") }
    var x0Text by remember { mutableStateOf("") }
    var intervalA by remember { mutableStateOf("") }
    var intervalB by remember { mutableStateOf("") }
    var nPoints by remember { mutableStateOf("8") }

    val inputType = viewModel.inputType.collectAsState().value
    val functionType = viewModel.functionType.collectAsState().value

    BorderBox(modifier = Modifier.width(22.cssRem)) {
        Column {
            AppLabel("Ввод данных", modifier = Modifier.padding(bottom = 0.75.cssRem))

            Row(modifier = Modifier.padding(bottom = 0.5.cssRem)) {
                InputType.values().forEach { type ->
                    AppButton(
                        onClick = { viewModel.setInputType(type) },
                        modifier = Modifier.padding(right = 0.5.cssRem)
                    ) {
                        AppText(type.name, fontSize = 1.0)
                    }
                }
            }

            if (inputType == InputType.MANUAL) {
                AppSecondaryText(
                    "Формат: x y (по одной паре на строку)",
                    modifier = Modifier.padding(bottom = 0.5.cssRem)
                )
                AppText(
                    text = inputText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(7.cssRem)
                        .padding(bottom = 0.5.cssRem),
                    fontSize = 1.0
                )
                AppButton(
                    onClick = {
                        val points = inputText.lines()
                            .mapNotNull { line ->
                                val parts = line.trim().split(Regex("\\s+"))
                                if (parts.size == 2) {
                                    parts[0].toDoubleOrNull()?.let { x ->
                                        parts[1].toDoubleOrNull()?.let { y ->
                                            DataPoint(x, y)
                                        }
                                    }
                                } else null
                            }
                        viewModel.setPoints(points)
                    },
                    modifier = Modifier.padding(bottom = 0.5.cssRem)
                ) {
                    AppText("Задать точки", fontSize = 1.0)
                }
            }

            if (inputType == InputType.FUNCTION) {
                Row(modifier = Modifier.padding(bottom = 0.5.cssRem)) {
                    FunctionType.values().forEach { type ->
                        AppButton(
                            onClick = { viewModel.setFunctionType(type) },
                            modifier = Modifier.padding(right = 0.5.cssRem)
                        ) {
                            AppText(type.name, fontSize = 1.0)
                        }
                    }
                }
                Row(modifier = Modifier.padding(bottom = 0.5.cssRem)) {
                    AppNumberField(
                        value = intervalA,
                        onValueChanged = { intervalA = it },
                        modifier = Modifier.width(4.5.cssRem).padding(right = 0.5.cssRem)
                    )
                    AppNumberField(
                        value = intervalB,
                        onValueChanged = { intervalB = it },
                        modifier = Modifier.width(4.5.cssRem).padding(right = 0.5.cssRem)
                    )
                    AppNumberField(
                        value = nPoints,
                        onValueChanged = { nPoints = it },
                        modifier = Modifier.width(4.5.cssRem)
                    )
                }
                AppButton(
                    onClick = {
                        val a = intervalA.toDoubleOrNull() ?: 0.0
                        val b = intervalB.toDoubleOrNull() ?: 1.0
                        val n = nPoints.toIntOrNull() ?: 8
                        viewModel.generatePointsByFunction(a, b, n)
                    },
                    modifier = Modifier.padding(bottom = 0.5.cssRem)
                ) {
                    AppText("Сгенерировать точки", fontSize = 1.0)
                }
            }

            Row(modifier = Modifier.padding(bottom = 0.5.cssRem)) {
                AppNumberField(
                    value = x0Text,
                    onValueChanged = {
                        x0Text = it
                        it.toDoubleOrNull()?.let(viewModel::setX0)
                    },
                    modifier = Modifier.width(7.cssRem)
                )
            }

            AppButton(
                onClick = { viewModel.calculate() },
                modifier = Modifier.fillMaxWidth()
            ) {
                AppText("Вычислить", fontSize = 1.0)
            }
        }
    }
}
