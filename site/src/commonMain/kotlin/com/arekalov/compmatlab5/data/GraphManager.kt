package com.arekalov.compmatlab5.data

expect class GraphManager() {
    fun initGraph()
    fun clearGraph()
//    fun plotPoints(points: List<Point>)
    fun plotFunction(expression: String, color: String = "#0C24A4", hidden: Boolean = true)
    fun setTheme(isDark: Boolean)
    fun jsLog(value: String)
} 