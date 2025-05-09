package com.arekalov.compmatlab5.pages

import androidx.compose.runtime.Composable
import com.arekalov.compmatlab5.common.PAGE_TITLE
import com.arekalov.compmatlab5.components.layouts.PageLayout
import com.arekalov.compmatlab5.components.sections.DataInputSection
import com.arekalov.compmatlab5.components.sections.GraphSection
import com.arekalov.compmatlab5.components.sections.ResultSection
import com.arekalov.compmatlab5.viewmodel.InterpolationViewModel
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.foundation.layout.*
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.compose.ui.Alignment
import org.jetbrains.compose.web.css.*

@Page
@Composable
fun Index() {
    val viewModel = InterpolationViewModel()
    PageLayout(
        title = PAGE_TITLE,
        onThemeChanged = viewModel::setTheme
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(1.cssRem),
            modifier = Modifier
                .padding(1.cssRem)
                .fillMaxSize(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(1.cssRem),
                verticalAlignment = Alignment.Top
            ) {
                DataInputSection(
                    modifier = Modifier
                        .width(30.cssRem)
                        .height(40.cssRem),
                    viewModel = viewModel
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.cssRem)
                ) {
                    GraphSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                    )
                }
            }

            ResultSection(viewModel = viewModel)
        }
    }
}
