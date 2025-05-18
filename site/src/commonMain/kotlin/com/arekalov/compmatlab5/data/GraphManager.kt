package com.arekalov.compmatlab5.data

import com.arekalov.compmatlab5.models.DataPoint

expect class GraphManager() {
    fun initGraph()
    fun clearGraph()
    fun plotPoints(id: String, points: List<DataPoint>, colorValue: String, isLinesEnabled: Boolean = false)
    fun setTheme(isDark: Boolean)
    fun jsLog(value: String)
} 