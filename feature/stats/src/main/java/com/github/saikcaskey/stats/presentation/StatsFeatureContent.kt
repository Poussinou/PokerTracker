package com.github.saikcaskey.stats.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.saikcaskey.ui_charts.presentation.BalanceByPeriodChart

@Composable
fun StatsFeatureContent(
    component: StatsFeatureComponent,
) {
    val uiState = component.uiState.collectAsStateWithLifecycle()
    StatsScreenContent(uiState = uiState.value)
}

@Composable
fun StatsScreenContent(
    uiState: StatsFeatureComponent.UiState,
) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Text("balanceDataSmall")
        BalanceByPeriodChart(data = uiState.balanceDataSmall)
        HorizontalDivider()
        Text("balanceDataMedium")
        BalanceByPeriodChart(data = uiState.balanceDataMedium)
        HorizontalDivider()
        Text("balanceDataLarge")
        BalanceByPeriodChart(data = uiState.balanceDataLarge)
        HorizontalDivider()
        Text("uiState: $uiState")
        HorizontalDivider()
    }
}
