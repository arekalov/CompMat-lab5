package com.arekalov.compmatlab5.components.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.arekalov.compmatlab5.common.formatNumber
import com.arekalov.compmatlab5.components.widgets.*
import com.arekalov.compmatlab5.viewmodel.InterpolationViewModel
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import org.jetbrains.compose.web.css.cssRem

@Composable
fun ResultSection(viewModel: InterpolationViewModel) {
    val points = viewModel.points.collectAsState().value
    val result = viewModel.result.collectAsState().value
    val error = viewModel.error.collectAsState().value
    val table = viewModel.finiteDifferenceTable.collectAsState().value

    BorderBox(
        contentAlignment = Alignment.TopStart,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(1.cssRem)
        ) {
            AppLabel(
                "Результаты",
                modifier = Modifier.padding(bottom = 1.cssRem),
                color = AppColors.Primary,
                fontSize = 1.5
            )

            if (error != null) {
                AppText("Ошибка: $error", color = AppColors.Error, modifier = Modifier.padding(bottom = 1.cssRem))
            }

            if (points.isNotEmpty()) {
                AppSecondaryText(
                    "Сгенерированные точки:",
                    modifier = Modifier.padding(top = 0.5.cssRem, bottom = 0.5.cssRem)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.5.cssRem)
                ) {
                    points.forEach { point ->
                        AppText("x = ${formatNumber(point.x, 4)}, y = ${formatNumber(point.y, 4)}")
                    }
                }
            }

            if (table != null) {
                AppSecondaryText("Таблица конечных разностей:", modifier = Modifier.padding(bottom = 0.5.cssRem))
                val cellWidth = 6.cssRem
                Box(
                    modifier = Modifier
                        .padding(bottom = 1.cssRem)
                        .overflow(Overflow.Auto)
                        .fillMaxWidth()
                ) {
                    Column {
                        Row {
                            AppSecondaryText("x", modifier = Modifier.width(cellWidth).padding(top = 0.25.cssRem, bottom = 0.25.cssRem).textAlignCenter())
                            table.x.forEach { x ->
                                AppSecondaryText(
                                    formatNumber(x, 4),
                                    modifier = Modifier.width(cellWidth).padding(top = 0.25.cssRem, bottom = 0.25.cssRem).textAlignCenter()
                                )
                            }
                        }
                        table.differences.forEachIndexed { i, row ->
                            Row {
                                AppSecondaryText("Δ$i", modifier = Modifier.width(cellWidth).padding(top = 0.25.cssRem, bottom = 0.25.cssRem).textAlignCenter())
                                row.forEach { diff ->
                                    AppText(
                                        formatNumber(diff, 4),
                                        modifier = Modifier.width(cellWidth).padding(top = 0.25.cssRem, bottom = 0.25.cssRem).textAlignCenter()
                                    )
                                }
                            }
                        }
                    }
                }
                // t после таблицы
                val points = viewModel.points.collectAsState().value
                val x0 = viewModel.x0.collectAsState().value
                if (points.isNotEmpty() && x0 != null && result != null) {
                    val h = points[1].x - points[0].x
                    AppSecondaryText("t для равномерной сетки:", modifier = Modifier.padding(bottom = 0.25.cssRem))
                    AppText(
                        "t = (x₀ - x₀*) / h",
                        modifier = Modifier.padding(bottom = 0.1.cssRem)
                    )
                    AppText(
                        "t = (${formatNumber(x0)} - ${formatNumber(points[0].x)}) / ${formatNumber(h)} = ${
                            formatNumber(
                                result.tValue
                            )
                        }",
                        modifier = Modifier.padding(bottom = 1.cssRem)
                    )
                }
            }

            if (result != null) {
                // Метод Лагранжа
                AppSecondaryText("Метод Лагранжа:", modifier = Modifier.padding(bottom = 0.5.cssRem))
                AppText(
                    "Значение в x₀: ${formatNumber(result.lagrangeValue)}",
                    color = AppColors.Success,
                    modifier = Modifier.padding(bottom = 1.cssRem)
                )

                // Метод Ньютона
                AppSecondaryText("Метод Ньютона:", modifier = Modifier.padding(bottom = 0.5.cssRem))
                AppText(
                    "Значение в x₀: ${formatNumber(result.newtonValue)}",
                    color = AppColors.Success,
                    modifier = Modifier.padding(bottom = 0.5.cssRem)
                )
            }
        }
    }
}

fun Modifier.textAlignCenter() = this.then(Modifier.textAlign(TextAlign.Center)) 