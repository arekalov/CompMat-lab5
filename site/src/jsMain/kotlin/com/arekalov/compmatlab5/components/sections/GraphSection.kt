package com.arekalov.compmatlab5.components.sections

import androidx.compose.runtime.Composable
import com.arekalov.compmatlab5.common.StringResources
import com.arekalov.compmatlab5.components.widgets.AppText
import com.arekalov.compmatlab5.components.widgets.BorderBox
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div

@Composable
fun GraphSection(
    modifier: Modifier = Modifier
) {
    BorderBox(modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(1.cssRem),
            modifier = Modifier.fillMaxSize()
        ) {
            AppText(StringResources.GRAPH_TITLE, fontSize = 1.5)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Div(
                    attrs = {
                        id("calculator")
                        style {
                            width(100.percent)
                            height(100.percent)
                        }
                    }
                )
            }
        }
    }
}