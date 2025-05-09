package com.arekalov.compmatlab5.components.sections

import androidx.compose.runtime.Composable
import com.arekalov.compmatlab5.common.GITHUB_URI
import com.arekalov.compmatlab5.toSitePalette
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.navigation.UncoloredLinkVariant
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.base
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.style.vars.color.ColorVar
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.dom.Span

val FooterStyle = CssStyle.base {
    backgroundColor(colorMode.toSitePalette().cobweb)
        .padding(topBottom = 1.cssRem, leftRight = 10.percent)
}

@Composable
fun Footer(modifier: Modifier = Modifier) {
    Box(FooterStyle.toModifier().then(modifier), contentAlignment = Alignment.Center) {
        Span(Modifier.textAlign(TextAlign.Center).toAttrs()) {
            val sitePalette = ColorMode.current.toSitePalette()
            SpanText("$MADE_BY_STR ")
            Link(
                GITHUB_URI,
                SOURCE_CODE_STR,
                Modifier.setVariable(ColorVar, sitePalette.brand.primary).whiteSpace(WhiteSpace.NoWrap),
                variant = UncoloredLinkVariant
            )
        }
    }
}
