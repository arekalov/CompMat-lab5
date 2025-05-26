package com.arekalov.compmatlab5.components.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.arekalov.compmatlab5.common.formatNumber
import com.arekalov.compmatlab5.components.widgets.*
import com.arekalov.compmatlab5.theme.getColor
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
import com.arekalov.compmatlab5.common.StringResources

@Composable
fun ResultSection(viewModel: InterpolationViewModel) {
    val state = viewModel.state.collectAsState().value
    val points = state.points
    val x0 = state.x0
    val table = state.finiteDifferenceTable
    val results = state.interpolationResults
    val error = state.error

    BorderBox(
        contentAlignment = Alignment.TopStart,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(1.cssRem)
        ) {
            AppLabel(
                StringResources.RESULTS_LABEL,
                modifier = Modifier.padding(bottom = 1.cssRem),
                color = AppColors.Primary,
                fontSize = 1.5
            )

            if (error != null) {
                AppText(StringResources.ERROR_PREFIX + error, color = AppColors.Error, modifier = Modifier.padding(bottom = 1.cssRem))
            }

            if (points.isNotEmpty()) {
                AppSecondaryText(
                    StringResources.GENERATED_POINTS_LABEL,
                    modifier = Modifier.padding(top = 0.5.cssRem, bottom = 0.5.cssRem)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.5.cssRem)
                ) {
                    points.forEach { point ->
                        AppText("${formatNumber(point.x, 4)} ${formatNumber(point.y, 4)}")
                    }
                }
            }

            if (table != null) {
                AppSecondaryText(StringResources.FINITE_DIFF_TABLE_LABEL, modifier = Modifier.padding(bottom = 0.5.cssRem))
                val cellWidth = 6.cssRem
                Box(
                    modifier = Modifier
                        .padding(bottom = 1.cssRem)
                        .overflow(Overflow.Auto)
                        .fillMaxWidth()
                ) {
                    Column {
                        Row {
                            AppSecondaryText(
                                StringResources.X_LABEL,
                                modifier = Modifier.width(cellWidth).padding(top = 0.25.cssRem, bottom = 0.25.cssRem)
                                    .textAlignCenter()
                            )
                            table.x.forEach { x ->
                                AppSecondaryText(
                                    formatNumber(x, 4),
                                    modifier = Modifier.width(cellWidth)
                                        .padding(top = 0.25.cssRem, bottom = 0.25.cssRem).textAlignCenter()
                                )
                            }
                        }
                        table.differences.forEachIndexed { i, row ->
                            Row {
                                AppSecondaryText(
                                    StringResources.DELTA_LABEL_PREFIX + "$i",
                                    modifier = Modifier.width(cellWidth)
                                        .padding(top = 0.25.cssRem, bottom = 0.25.cssRem).textAlignCenter()
                                )
                                row.forEach { diff ->
                                    AppText(
                                        formatNumber(diff, 4),
                                        modifier = Modifier.width(cellWidth)
                                            .padding(top = 0.25.cssRem, bottom = 0.25.cssRem).textAlignCenter()
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (results.isNotEmpty()) {
                results.forEach { result ->
                    AppSecondaryText("${result.method}:", modifier = Modifier.padding(bottom = 0.5.cssRem))
                    AppText(
                        StringResources.VALUE_AT_X0_LABEL + formatNumber(result.value),
                        color = result.method.getColor(isDark = viewModel.isDarkTheme),
                        modifier = Modifier.padding(bottom = 0.5.cssRem)
                    )
                }
            }
        }
    }
}

fun Modifier.textAlignCenter() = this.then(Modifier.textAlign(TextAlign.Center)) 