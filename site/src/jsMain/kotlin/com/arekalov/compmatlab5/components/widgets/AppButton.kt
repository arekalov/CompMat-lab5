package com.arekalov.compmatlab5.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.silk.components.forms.Button
import org.jetbrains.compose.web.css.cssRem


@Composable
fun AppButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Button(
        onClick = { onClick() },
        modifier = modifier
            .fillMaxWidth()
            .margin(top = 1.cssRem)
    ) {
        content()
    }
}

