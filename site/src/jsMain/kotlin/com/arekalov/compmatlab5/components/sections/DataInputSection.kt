package com.arekalov.compmatlab5.components.sections

import androidx.compose.runtime.*
import com.arekalov.compmatlab5.common.formatNumber
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
import com.arekalov.compmatlab5.common.StringResources

@Composable
fun DataInputSection(
    modifier: Modifier = Modifier,
    viewModel: InterpolationViewModel
) {
    var inputText by remember { mutableStateOf("") }
    var x0Text by remember { mutableStateOf("") }
    var intervalA by remember { mutableStateOf("0") }
    var intervalB by remember { mutableStateOf("1") }
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
                StringResources.DATA_INPUT_LABEL,
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
                        onClick = {
                            viewModel.setInputType(type)
                            viewModel.clearState()
                            nPoints = "8"
                            intervalA = "0"
                            intervalB = "1"
                            x0Text = ""
                        },
                        modifier = Modifier.padding(right = 0.5.cssRem)
                    ) {
                        AppLabel(type.name, fontSize = 1.0, color = AppColors.Primary)
                    }
                }
            }

            when (inputType) {
                InputType.MANUAL -> {
                    AppSecondaryText(
                        StringResources.MANUAL_FORMAT_HINT,
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
                            viewModel.updatePoints(points)
                        },
                        modifier = Modifier.padding(top = 0.5.cssRem)
                    ) {
                        AppText(StringResources.SET_POINTS_BUTTON, fontSize = 1.0)
                    }
                }

                InputType.FILE -> {
                    AppSecondaryText(
                        StringResources.FILE_FORMAT_HINT,
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
                                            points?.let { viewModel.updatePoints(it) }
                                        }
                                        reader.readAsText(file)
                                    }
                                }
                            }
                    )
                }

                InputType.FUNCTION -> {
                    AppSecondaryText(
                        StringResources.FUNCTION_HINT,
                        modifier = Modifier.padding(bottom = 0.5.cssRem)
                    )
                    Row(
                        modifier = Modifier.padding(bottom = 0.5.cssRem),
                        horizontalArrangement = Arrangement.spacedBy(1.cssRem)
                    ) {
                        FunctionType.values().forEach { type ->
                            AppButton(
                                isEnabled = functionType != type,
                                onClick = {
                                    viewModel.run {
                                        clearState()
                                        setFunctionType(type)
                                    }
                                },
                                modifier = Modifier.padding(right = 0.5.cssRem)
                            ) {
                                AppText(
                                    type.name
                                )
                            }
                        }
                    }
                    Column(modifier = Modifier.padding(bottom = 0.5.cssRem)) {
                        AppSecondaryText(StringResources.INTERVAL_LABEL, modifier = Modifier.padding(bottom = 0.25.cssRem))
                        AppSecondaryText(StringResources.INTERVAL_LABEL, modifier = Modifier.padding(bottom = 0.25.cssRem))
                        AppSecondaryText(StringResources.NUM_POINTS_LABEL, modifier = Modifier.padding(bottom = 0.25.cssRem))
                        AppNumberField(
                            value = intervalA,
                            onValueChanged = { intervalA = it },
                            modifier = Modifier.width(4.5.cssRem)
                        )
                        AppNumberField(
                            value = intervalB,
                            onValueChanged = { intervalB = it },
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
                        AppText(StringResources.GENERATE_POINTS_BUTTON, fontSize = 1.0)
                    }
                }
            }

            AppSecondaryText(
                StringResources.X0_LABEL,
                modifier = Modifier.padding(top = 1.cssRem, bottom = 0.5.cssRem)
            )
            AppNumberField(
                value = x0Text,
                onValueChanged = {
                    x0Text = it
                    it.toDoubleOrNull()?.let(viewModel::updateX0)
                },
                modifier = Modifier.width(7.cssRem)
            )

            AppButton(
                onClick = { viewModel.calculate() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 1.cssRem)
            ) {
                AppText(StringResources.CALCULATE_BUTTON, fontSize = 1.0)
            }
        }
    }
}
