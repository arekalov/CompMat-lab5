package com.arekalov.compmatlab5.theme

import com.varabyte.kobweb.compose.ui.graphics.Color

interface ThemeColors {
    // String representations
    val errorString: String
    val errorIversedString: String
    val successString: String
    val successSInversedtSring: String
    val primaryString: String
    val primaryInversedString: String
    val secondaryString: String
    val secondaryInversedString: String
    val warningString: String
    val warningInversedString: String

    // Color objects
    val errorColor: Color
    val errorInversedColor: Color
    val successColor: Color
    val successInversedColor: Color
    val primaryColor: Color
    val primaryInversedColor: Color
    val secondaryColor: Color
    val secondaryInversedColor: Color
    val warningColor: Color
    val warningInversedColor: Color
} 