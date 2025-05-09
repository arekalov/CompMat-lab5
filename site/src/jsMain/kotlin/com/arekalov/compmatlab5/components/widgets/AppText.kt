package com.arekalov.compmatlab5.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.cssRem

@Composable
fun AppText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: Double = 1.0,
    color: Color = AppColors.Primary
) {
    SpanText(
        text = text,
        modifier = modifier
            .color(color)
            .fontSize(fontSize.cssRem)
    )
} 