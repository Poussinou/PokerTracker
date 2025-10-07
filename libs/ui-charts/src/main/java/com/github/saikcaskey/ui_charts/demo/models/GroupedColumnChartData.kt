package com.github.saikcaskey.ui_charts.demo.models

data class GroupedColumnChartData(
    val xSeriesData: List<Int> = sampleMultiColumnXSeriesData,
    val ySeriesData: Map<String, List<Number>> = sampleMultiColumnSeriesData,
)

private val sampleMultiColumnXSeriesData = (2010..2023).toList()

private val sampleMultiColumnSeriesData = mapOf(
    "group1" to listOf<Number>(2.2, 2.3, 2.4, 2.6, 2.5, 2.3, 2.2, 2.2, 2.2, 2.1, 2),
    "group2" to listOf<Number>(0.3, 0.3, 0.4, 0.8, 1.6, 2.3, 2.6, 2.8, 3.1, 3.3, 3.6),
    "group3" to listOf<Number>(0.2, 0.3, 0.4, 0.3, 0.3, 0.3, 0.3, 0.4, 0.4, 0.6, 0.7),
)
