package com.github.saikcaskey.ui_charts.demo.models

import kotlin.math.pow

data class ComboChartData(
    val columnSeriesData: List<Number> = (3..12).map { (it) },
    val lineSeriesData: List<Number> = (3..12).map { (it * 2.toDouble().pow(it * 2)) },
)
