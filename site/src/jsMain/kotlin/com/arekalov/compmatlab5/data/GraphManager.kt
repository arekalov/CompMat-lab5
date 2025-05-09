package com.arekalov.compmatlab5.data

import kotlinx.browser.document
import org.w3c.dom.HTMLScriptElement

private const val DESMOS_API_URI = "https://www.desmos.com/api/v1.10/calculator.js?apiKey=dcb31709b452b1cf9dc26972add0fda6"
private const val CALCULATOR_DIV_ID = "calculator"

private var calculator: dynamic = null

actual class GraphManager {
    actual fun initGraph() {
        val script = document.createElement("script") as HTMLScriptElement
        script.src = DESMOS_API_URI
        script.onload = {
            val elt = document.getElementById(CALCULATOR_DIV_ID)
            if (elt != null) {
                calculator = js(
                    """
                    new Desmos.GraphingCalculator(elt, {
                        invertedColors: true,
                        expressions: true,
                        settingsMenu: false,
                        expressionsCollapsed: true,
                        zoomButtons: false,
                        border: false
                    })
                """
                )
            }
        }
        document.body?.appendChild(script)
    }

    actual fun clearGraph() {
        calculator?.setBlank()
    }

    actual fun plotPoints(points: List<Point>) {
        val pointsStr = points.joinToString(",") { "(${it.x},${it.y})" }
        calculator?.setExpression(js("""
            {
                id: "points",
                latex: pointsStr,
                style: "points",
                color: "#0C24A4"
            }
        """))
    }

    actual fun plotFunction(expression: String, color: String, hidden: Boolean) {
        calculator?.setExpression(js("""
        {
            id: expression,
            latex: expression,
            color: color,
            hidden: hidden
        }
    """))
    }

    actual fun setTheme(isDark: Boolean) {
        calculator?.updateSettings(js("{'invertedColors': isDark}"))
    }

    actual fun jsLog(value: String) {
        js("console.log(value)")
    }
} 