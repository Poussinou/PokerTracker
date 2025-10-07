package com.github.saikcaskey.ui_charts.demo.models

data class PairedColumnChartData(
    val seriesData: Map<String, Int> = samplePairedColumnChartData,
)

private val samplePairedColumnChartData = mapOf(
    "Ag" to 22378,
    "Mo" to 4478,
    "U" to 3624,
    "Sn" to 2231,
    "Li" to 1634,
    "W" to 1081
)
