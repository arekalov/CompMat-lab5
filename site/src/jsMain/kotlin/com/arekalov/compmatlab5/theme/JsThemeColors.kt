package com.arekalov.compmatlab5.theme

import com.arekalov.compmatlab5.components.widgets.AppColors
import com.arekalov.compmatlab5.models.InterpolationMethod
import com.varabyte.kobweb.compose.ui.graphics.Color


fun InterpolationMethod.getColorString(isDark: Boolean): String {
    return when (this) {
        InterpolationMethod.LagrangeInterpolation -> if (isDark) AppColors.primaryInversedString else AppColors.primaryString
        InterpolationMethod.NewtonDividedDifferenceInterpolation -> if (isDark) AppColors.successInversedString else AppColors.successString
        InterpolationMethod.GaussInterpolation -> if (isDark) AppColors.secondaryInversedString else AppColors.secondaryString
    }
}

fun InterpolationMethod.getColor(isDark: Boolean): Color {
    return when (this) {
        InterpolationMethod.LagrangeInterpolation -> if (isDark) AppColors.Primary else AppColors.PrimaryInversed
        InterpolationMethod.NewtonDividedDifferenceInterpolation -> if (isDark) AppColors.Success else AppColors.SuccessInversed
        InterpolationMethod.GaussInterpolation -> if (isDark) AppColors.Secondary else AppColors.SecondaryInversed
    }
}