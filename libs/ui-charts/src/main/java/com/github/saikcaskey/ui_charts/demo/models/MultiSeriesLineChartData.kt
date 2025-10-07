package com.github.saikcaskey.ui_charts.demo.models

data class MultiSeriesLineChartData(
    val seriesData: Map<String, Map<Int, Number>> = sampleMultiSeriesLineChartData,
)

private val sampleMultiSeriesLineChartData = mapOf<String, Map<Int, Number>>(
    "group1" to mapOf(
        2009 to -100,
        2012 to -44.16,
        2014 to -6.8,
        2015 to 0.69,
        2016 to 6.62,
        2018 to 11.69,
        2019 to 9.52,
        2020 to 16.45,
    ),
    "group2" to mapOf(
        2019 to -100,
        2021 to 2.73,
        2022 to 8.2
    ),
    "group3" to mapOf(
        2020 to -120,
        2021 to -100,
        2022 to -48.04,
        2023 to -12.64
    ),
)
