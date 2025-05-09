package com.arekalov.compmatlab5.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.dom.TextInput


@Composable
fun AppNumberField(
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextInput(
        value = value,
        attrs = modifier.toAttrs {
            onInput { event ->
                onValueChanged(event.value)
            }
        }
    )
} 