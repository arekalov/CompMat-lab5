package com.arekalov.compmatlab5.components.widgets

import androidx.compose.runtime.Composable
import com.arekalov.compmatlab5.toSitePalette
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.cssRem

@Composable
fun BorderBox(
    modifier: Modifier = Modifier,
    color: Color = ColorMode.current.toSitePalette().brand.accent,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable () -> Unit,
) {
    Box(
        contentAlignment = contentAlignment,
        modifier = modifier
            .padding(1.cssRem)
            .borderRadius(1.cssRem)
            .border(0.1.cssRem, style = LineStyle.Solid, color = color)
    ) {
        content()
    }
}