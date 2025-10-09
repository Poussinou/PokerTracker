package com.github.saikcaskey.ui_charts.demo.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SampleCharts() {
    Column {
        Text("PairedColumnChart")
        PairedColumnChart()
        Text("ColumnLineChart")
        ColumnLineChart()
        Text("GroupedColumnChart")
        GroupedColumnChart()
        Text("MultiSeriesLineChart")
        MultiSeriesLineChart()
        Text("PercentageCoveredLineChart")
        PercentageCoveredLineChart()
    }
}
