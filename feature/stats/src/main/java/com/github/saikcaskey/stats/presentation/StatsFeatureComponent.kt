package com.github.saikcaskey.stats.presentation

import com.github.saikcaskey.pokertracker.domain.models.Expense
import com.github.saikcaskey.pokertracker.domain.presentation.MainPagerPageComponent
import kotlinx.coroutines.flow.StateFlow

interface StatsFeatureComponent : MainPagerPageComponent {
    val uiState: StateFlow<UiState>

    data class UiState(
        val balanceDataSmall: Map<Int, Double> = emptyMap(),
        val balanceDataMedium: Map<Int, Double> = emptyMap(),
        val balanceDataLarge: Map<Int, Double> = emptyMap(),
        val recentExpenses: List<Expense> = emptyList(),
    )
}
