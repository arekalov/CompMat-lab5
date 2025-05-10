package com.arekalov.compmatlab5.components.sections

import androidx.compose.runtime.*
import com.arekalov.compmatlab5.models.*
import com.arekalov.compmatlab5.viewmodel.InterpolationViewModel
import com.arekalov.compmatlab5.components.widgets.*
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.App
import org.jetbrains.compose.web.attributes.accept
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.dom.TextArea
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.FileInput

@Composable
fun DataInputSection(
    modifier: Modifier = Modifier,
    viewModel: InterpolationViewModel
) {
    var inputText by remember { mutableStateOf("") }
    var x0Text by remember { mutableStateOf("") }
    var intervalA by remember { mutableStateOf("") }
    var intervalB by remember { mutableStateOf("") }
    var nPoints by remember { mutableStateOf("8") }

    val inputType = viewModel.inputType.collectAsState().value
    val functionType = viewModel.functionType.collectAsState().value

    BorderBox(
        modifier = modifier,
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
        ) {
            AppLabel(
                "Ввод данных",
                modifier = Modifier.padding(bottom = 1.cssRem),
                fontSize = 1.5,
                color = AppColors.Primary
            )

            Row(
                modifier = Modifier.padding(bottom = 1.cssRem),
                horizontalArrangement = Arrangement.spacedBy(0.5.cssRem)
            ) {
                InputType.values().forEach { type ->
                    AppButton(
                        isEnabled = inputType != type,
                        onClick = { viewModel.setInputType(type) },
                        modifier = Modifier.padding(right = 0.5.cssRem)
                    ) {
                        AppLabel(type.name, fontSize = 1.0, color = AppColors.Primary)
                    }
                }
            }

            when (inputType) {
                InputType.MANUAL -> {
                    AppSecondaryText(
                        "Формат: x y (по одной паре на строку)",
                        modifier = Modifier.padding(bottom = 0.5.cssRem)
                    )
                    TextArea(
                        value = inputText,
                        attrs = Modifier
                            .fillMaxWidth()
                            .height(10.cssRem)
                            .padding(0.5.cssRem)
                            .toAttrs {
                                onInput { event ->
                                    inputText = event.value
                                }
                            }
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
                        modifier = Modifier.padding(top = 0.5.cssRem)
                    ) {
                        AppText("Задать точки", fontSize = 1.0)
                    }
                }

                InputType.FILE -> {
                    AppSecondaryText(
                        "Выберите текстовый файл с точками в формате: x y (по одной паре на строку)",
                        modifier = Modifier.padding(bottom = 0.5.cssRem)
                    )
                    FileInput(
                        attrs = Modifier
                            .fillMaxWidth()
                            .padding(0.5.cssRem)
                            .toAttrs {
                                accept("text/plain")
                                onChange { event ->
                                    val file = event.target.files?.item(0)
                                    if (file != null) {
                                        val reader = org.w3c.files.FileReader()
                                        reader.onload = { e ->
                                            val content = e.target?.asDynamic()?.result as? String
                                            val points = content?.lines()
                                                ?.mapNotNull { line ->
                                                    val parts = line.trim().split(Regex("\\s+"))
                                                    if (parts.size == 2) {
                                                        val x = parts[0].toDoubleOrNull()
                                                        val y = parts[1].toDoubleOrNull()
                                                        if (x != null && y != null) {
                                                            DataPoint(x, y)
                                                        } else null
                                                    } else null
                                                }
                                            points?.let { viewModel.setPoints(it) }
                                        }
                                        reader.readAsText(file)
                                    }
                                }
                            }
                    )

                    // Отображение загруженных точек
                    val points = viewModel.points.collectAsState().value
                    if (points.isNotEmpty()) {
                        AppSecondaryText(
                            "Загруженные точки:",
                            modifier = Modifier.padding(top = 1.cssRem, bottom = 0.5.cssRem)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.cssRem)
                                .padding(0.5.cssRem)
                        ) {
                            points.forEach { point ->
                                AppText("x = ${point.x}, y = ${point.y}")
                            }
                        }
                    }
                }

                InputType.FUNCTION -> {
                    AppSecondaryText(
                        "Выберите функцию и параметры",
                        modifier = Modifier.padding(bottom = 0.5.cssRem)
                    )
                    Row(
                        modifier = Modifier.padding(bottom = 0.5.cssRem),
                        horizontalArrangement = Arrangement.spacedBy(1.cssRem)
                    ) {
                        FunctionType.values().forEach { type ->
                            AppButton(
                                isEnabled = functionType != type,
                                onClick = { viewModel.setFunctionType(type) },
                                modifier = Modifier.padding(right = 0.5.cssRem)
                            ) {
                                AppText(
                                    type.name
                                )
                            }
                        }
                    }
                    Column(modifier = Modifier.padding(bottom = 0.5.cssRem)) {
                        AppSecondaryText("Интервал [a, b]:", modifier = Modifier.padding(bottom = 0.25.cssRem))
                        Row(modifier = Modifier.padding(bottom = 0.5.cssRem)) {
                            AppNumberField(
                                value = intervalA,
                                onValueChanged = { intervalA = it },
                                modifier = Modifier.width(4.5.cssRem).padding(right = 0.5.cssRem)
                            )
                            AppNumberField(
                                value = intervalB,
                                onValueChanged = { intervalB = it },
                                modifier = Modifier.width(4.5.cssRem)
                            )
                        }
                        AppSecondaryText("Количество точек:", modifier = Modifier.padding(bottom = 0.25.cssRem))
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
                        }
                    ) {
                        AppText("Сгенерировать точки", fontSize = 1.0)
                    }
                }
            }

            AppSecondaryText(
                "Значение x₀ для интерполяции:",
                modifier = Modifier.padding(top = 1.cssRem, bottom = 0.5.cssRem)
            )
            AppNumberField(
                value = x0Text,
                onValueChanged = {
                    x0Text = it
                    it.toDoubleOrNull()?.let(viewModel::setX0)
                },
                modifier = Modifier.width(7.cssRem)
            )

            AppButton(
                onClick = { viewModel.calculate() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 1.cssRem)
            ) {
                AppText("Вычислить", fontSize = 1.0)
            }
        }
    }
}
