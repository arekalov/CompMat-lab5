package com.arekalov.compmatlab5.theme

import com.arekalov.compmatlab5.components.widgets.AppColors
import com.varabyte.kobweb.compose.ui.graphics.Color

object JsThemeColors : ThemeColors {
    override val errorString: String = AppColors.errorString
    override val errorIversedString: String = AppColors.errorInversedString
    override val successString: String = AppColors.successString
    override val successSInversedtSring: String = AppColors.successInversedString
    override val primaryString: String = AppColors.primaryString
    override val primaryInversedString: String = AppColors.primaryInversedString
    override val secondaryString: String = AppColors.secondaryString
    override val secondaryInversedString: String = AppColors.secondaryInversedString
    override val warningString: String = AppColors.warningString
    override val warningInversedString: String = AppColors.warningInversedString

    override val errorColor: Color
        get() = AppColors.Error
    override val errorInversedColor: Color
        get() = AppColors.ErrorInversed
    override val successColor: Color
        get() = AppColors.Success
    override val successInversedColor: Color
        get() = AppColors.SuccessInversed
    override val primaryColor: Color
        get() = AppColors.Primary
    override val primaryInversedColor: Color
        get() = AppColors.PrimaryInversed
    override val secondaryColor: Color
        get() = AppColors.Secondary
    override val secondaryInversedColor: Color
        get() = AppColors.SecondaryInversed
    override val warningColor: Color
        get() = AppColors.Warning
    override val warningInversedColor: Color
        get() = AppColors.WarningInversed
} 