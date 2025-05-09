package com.arekalov.compmatlab5.components.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.arekalov.compmatlab5.viewmodel.InterpolationViewModel
import com.arekalov.compmatlab5.components.widgets.*
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.foundation.layout.*
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.width
import org.jetbrains.compose.web.css.cssRem
import com.arekalov.compmatlab5.common.formatNumber
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize

@Composable
fun ResultSection(viewModel: InterpolationViewModel) {
    val result = viewModel.result.collectAsState().value
    val error = viewModel.error.collectAsState().value
    val table = viewModel.finiteDifferenceTable.collectAsState().value

    BorderBox(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(1.cssRem)) {
            AppLabel("Результаты", modifier = Modifier.padding(bottom = 1.cssRem))
            
            if (error != null) {
                AppText("Ошибка: $error", color = AppColors.Error, modifier = Modifier.padding(bottom = 1.cssRem))
            }

            if (table != null) {
                AppSecondaryText("Таблица конечных разностей:", modifier = Modifier.padding(bottom = 0.5.cssRem))
                Column(modifier = Modifier.padding(bottom = 1.cssRem)) {
                    Row {
                        AppSecondaryText("x", modifier = Modifier.width(3.5.cssRem))
                        table.x.forEach { x ->
                            AppSecondaryText(formatNumber(x), modifier = Modifier.width(3.5.cssRem))
                        }
                    }
                    table.differences.forEachIndexed { i, row ->
                        Row {
                            AppSecondaryText("Δ$i", modifier = Modifier.width(3.5.cssRem))
                            row.forEach { diff ->
                                AppText(formatNumber(diff), modifier = Modifier.width(3.5.cssRem))
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
                        "t = (${formatNumber(x0)} - ${formatNumber(points[0].x)}) / ${formatNumber(h)} = ${formatNumber(result.tValue)}",
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