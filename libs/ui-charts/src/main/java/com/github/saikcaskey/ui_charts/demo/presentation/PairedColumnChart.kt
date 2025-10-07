package com.github.saikcaskey.ui_charts.demo.presentation

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.saikcaskey.ui_charts.demo.models.PairedColumnChartData
import com.github.saikcaskey.ui_charts.demo.presentation.utils.rememberMarker
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.cartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.ColumnCartesianLayerMarkerTarget
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import java.text.DecimalFormat

private const val Y_DIVISOR = 1000
private val pairedColumnBottomAxisLabelKey = ExtraStore.Key<List<String>>()
private val pairedColumnYDecimalFormat = DecimalFormat("#.##K")
private val pairedColumnStartAxisValueFormatter = CartesianValueFormatter { _, value, _ ->
    pairedColumnYDecimalFormat.format(value / Y_DIVISOR)
}
private val pairedColumnBottomAxisValueFormatter = CartesianValueFormatter { context, x, _ ->
    context.model.extraStore[pairedColumnBottomAxisLabelKey][x.toInt()]
}
private val pairedColumnMarkerValueFormatter = DefaultCartesianMarker.ValueFormatter { _, targets ->
    val column = (targets[0] as ColumnCartesianLayerMarkerTarget).columns[0]
    SpannableStringBuilder()
        .append(
            pairedColumnYDecimalFormat.format(column.entry.y / Y_DIVISOR),
            ForegroundColorSpan(column.color),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
        )
}

@Composable
private fun PairedColumnChart(
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier = Modifier,
) {
    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberColumnCartesianLayer(
                ColumnCartesianLayer.ColumnProvider.series(
                    rememberLineComponent(fill = fill(Color(0xffff5500)), thickness = 16.dp)
                )
            ),
            startAxis = VerticalAxis.rememberStart(valueFormatter = pairedColumnStartAxisValueFormatter),
            bottomAxis = HorizontalAxis.rememberBottom(
                itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
                valueFormatter = pairedColumnBottomAxisValueFormatter,
            ),
            marker = rememberMarker(pairedColumnMarkerValueFormatter),
            layerPadding = { cartesianLayerPadding(scalableStart = 8.dp, scalableEnd = 8.dp) },
        ),
        modelProducer = modelProducer,
        modifier = modifier.height(220.dp),
        scrollState = rememberVicoScrollState(scrollEnabled = false),
    )
}

@Composable
fun PairedColumnChart(
    modifier: Modifier = Modifier,
    data: PairedColumnChartData = PairedColumnChartData(),
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            // Learn more: https://patrykandpatrick.com/eji9zq.
            columnSeries { series(data.seriesData.values) }
            extras { it[pairedColumnBottomAxisLabelKey] = data.seriesData.keys.toList() }
        }
    }
    PairedColumnChart(modelProducer, modifier)
}
