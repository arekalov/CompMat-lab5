package com.arekalov.compmatlab5.logic

import com.arekalov.compmatlab5.models.DataPoint
import kotlin.math.abs

object InterpolationUtils {
    fun factorial(n: Int): Int {
        return if (n <= 1) 1 else n * factorial(n - 1)
    }

    fun isUniformStep(points: List<DataPoint>): Boolean {
        if (points.size < 2) return true
        val step = points[1].x - points[0].x
        for (i in 1 until points.size) {
            if (abs(points[i].x - points[i-1].x - step) > 0.0001) {
                return false
            }
        }
        return true
    }

    fun getStep(points: List<DataPoint>): Double {
        return points[1].x - points[0].x
    }

    fun getMiddleIndex(points: List<DataPoint>): Int {
        return points.size / 2
    }

    fun finiteDifferences(points: List<DataPoint>): List<List<Double>> {
        val n = points.size - 1
        val finDifs = mutableListOf<List<Double>>()
        finDifs.add(points.map { it.y })
        
        for (k in 1..n) {
            val last = finDifs.last()
            finDifs.add((0 until n - k + 1).map { i ->
                last[i + 1] - last[i]
            })
        }
        
        return finDifs
    }

    fun dividedDifferences(points: List<DataPoint>): List<Double> {
        val n = points.size
        val coef = points.map { it.y }.toMutableList()
        
        for (j in 1 until n) {
            for (i in n-1 downTo j) {
                coef[i] = (coef[i] - coef[i-1]) / (points[i].x - points[i-j].x)
            }
        }
        
        return coef
    }

    fun getPlotPoints(method: InterpolationMethod, points: List<DataPoint>, a: Double, b: Double, dx: Double = 0.001): List<Pair<Double, Double>> {
        val result = mutableListOf<Pair<Double, Double>>()
        var x = a - dx
        while (x <= b + dx) {
            result.add(Pair(x, method.interpolate(points, x)))
            x += dx
        }
        return result
    }
} 