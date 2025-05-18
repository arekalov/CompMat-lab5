package com.arekalov.compmatlab5.data

import com.arekalov.compmatlab5.models.DataPoint

expect class GraphManager() {
    fun initGraph()
    fun clearGraph()
    fun plotPoints(id: String, points: List<DataPoint>, color: String = "#0C24A4", isLinesEnabled: Boolean = false)
    fun plotFunction(expression: String, color: String = "#0C24A4", hidden: Boolean = true)
    fun setTheme(isDark: Boolean)
    fun jsLog(value: String)
} 