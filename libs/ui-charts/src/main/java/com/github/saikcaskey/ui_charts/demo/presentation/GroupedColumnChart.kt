package com.github.saikcaskey.ui_charts.demo.presentation

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.saikcaskey.ui_charts.demo.models.GroupedColumnChartData
import com.github.saikcaskey.ui_charts.demo.presentation.utils.rememberMarker
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.cartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.stacked
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.component.shapeComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.insets
import com.patrykandpatrick.vico.compose.common.rememberHorizontalLegend
import com.patrykandpatrick.vico.compose.common.vicoTheme
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.LegendItem
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import java.text.DecimalFormat

private val LegendLabelKey = ExtraStore.Key<Set<String>>()
private val YDecimalFormat = DecimalFormat("#.## h")
private val StartAxisValueFormatter = CartesianValueFormatter.decimal(YDecimalFormat)
private val StartAxisItemPlacer = VerticalAxis.ItemPlacer.step({ 0.5 })
private val MarkerValueFormatter = DefaultCartesianMarker.ValueFormatter.default(YDecimalFormat)

@Composable
private fun GroupedColumnChart(
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier = Modifier,
) {
    val columnColors = listOf(Color(0xff6438a7), Color(0xff3490de), Color(0xff73e8dc))
    val legendItemLabelComponent = rememberTextComponent(vicoTheme.textColor)
    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberColumnCartesianLayer(
                columnProvider = ColumnCartesianLayer.ColumnProvider.series(
                    columnColors.map { color ->
                        rememberLineComponent(fill = fill(color), thickness = 16.dp)
                    }
                ),
                columnCollectionSpacing = 32.dp,
                mergeMode = { ColumnCartesianLayer.MergeMode.stacked() },
            ),
            startAxis = VerticalAxis.rememberStart(
                valueFormatter = StartAxisValueFormatter,
                itemPlacer = StartAxisItemPlacer,
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() }
            ),
            marker = rememberMarker(MarkerValueFormatter),
            layerPadding = { cartesianLayerPadding(scalableStart = 16.dp, scalableEnd = 16.dp) },
            legend = rememberHorizontalLegend(
                items = { extraStore ->
                    extraStore[LegendLabelKey].forEachIndexed { index, label ->
                        add(
                            LegendItem(
                                icon = shapeComponent(
                                    fill(columnColors[index]),
                                    CorneredShape.Pill
                                ),
                                labelComponent = legendItemLabelComponent,
                                label = label,
                            )
                        )
                    }
                },
                padding = insets(top = 16.dp),
            ),
        ),
        modelProducer = modelProducer,
        modifier = modifier.height(252.dp),
        zoomState = rememberVicoZoomState(zoomEnabled = false),
    )
}

@Composable
fun GroupedColumnChart(
    modifier: Modifier = Modifier,
    data: GroupedColumnChartData = GroupedColumnChartData(),
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            // Learn more: https://patrykandpatrick.com/eji9zq.
            columnSeries {
                data.ySeriesData.values.forEach {
                    series(data.xSeriesData, it)
                }
            }
            extras { extraStore -> extraStore[LegendLabelKey] = data.ySeriesData.keys }
        }
    }
    GroupedColumnChart(modelProducer, modifier)
}
