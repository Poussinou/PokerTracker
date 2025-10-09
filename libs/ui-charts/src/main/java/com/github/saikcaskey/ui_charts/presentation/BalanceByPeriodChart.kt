package com.github.saikcaskey.ui_charts.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.saikcaskey.ui_charts.presentation.utils.rememberMarker
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.Scroll
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer.Line
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer.LineFill
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer.LineProvider
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import kotlinx.datetime.LocalDate
import java.text.DecimalFormat

private val LegendLabelKey = ExtraStore.Key<List<Int>>()
private val YDecimalFormat = DecimalFormat("#.##")
private val StartAxisValueFormatter = CartesianValueFormatter.decimal(YDecimalFormat)
private val StartAxisItemPlacer = VerticalAxis.ItemPlacer.step({ 0.5 })
private val MarkerValueFormatter = DefaultCartesianMarker.ValueFormatter.default(YDecimalFormat)

private val bottomAxisValueFormatter = CartesianValueFormatter { context, x, y ->
    LocalDate.fromEpochDays(x.toInt()).dayOfYear.toString()
}

@Composable
fun BalanceByPeriodChart(
    modifier: Modifier = Modifier,
    data: Map<Int, Double>,
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            if (data.keys.isNotEmpty() && data.values.isNotEmpty()) {
                lineSeries { series(data.values) }
                columnSeries { series(data.keys, data.values) }
                extras { extraStore -> extraStore[LegendLabelKey] = data.keys.map { it } }
            }
        }
    }
    BalanceByPeriodChart(modelProducer, modifier)
}

@Composable
private fun BalanceByPeriodChart(
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier = Modifier,
    dailyBalanceLineFillColor: Color = MaterialTheme.colorScheme.secondary,
    expensesLineFillColor: Color = MaterialTheme.colorScheme.primary,
) {
    val expensesLine = rememberLineComponent(fill(expensesLineFillColor), 16.dp)
    val expensesPerDayColumnLayer = rememberColumnCartesianLayer(
        ColumnCartesianLayer.ColumnProvider.series(expensesLine)
    )
    val dailyBalanceLineLayer = rememberLineCartesianLayer(
        LineProvider.series(Line(LineFill.single(fill(dailyBalanceLineFillColor))))
    )
    CartesianChartHost(
        chart = rememberCartesianChart(
            layers = arrayOf(expensesPerDayColumnLayer, dailyBalanceLineLayer),
            startAxis = VerticalAxis.rememberStart(
                valueFormatter = StartAxisValueFormatter,
                itemPlacer = StartAxisItemPlacer,
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                valueFormatter = bottomAxisValueFormatter
            ),
            marker = rememberMarker(MarkerValueFormatter),
        ),
        zoomState = rememberVicoZoomState(zoomEnabled = false),
        scrollState = rememberVicoScrollState(initialScroll = Scroll.Absolute.End),
        animateIn = true,
        consumeMoveEvents = true,
        modelProducer = modelProducer,
        modifier = modifier,
    )
}
