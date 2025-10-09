package com.github.saikcaskey.stats.presentation

import com.arkivanov.decompose.ComponentContext
import com.github.saikcaskey.pokertracker.domain.CoroutineDispatchers
import com.github.saikcaskey.pokertracker.domain.extensions.minusDays
import com.github.saikcaskey.pokertracker.domain.repository.ExpenseRepository
import com.github.saikcaskey.pokertracker.domain.repository.StatsRepository
import com.github.saikcaskey.pokertracker.domain.util.nowAsLocalDateTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.plus

class StatsFeatureComponentImpl(
    componentContext: ComponentContext,
    statsRepository: StatsRepository,
    expenseRepository: ExpenseRepository,
    dispatchers: CoroutineDispatchers,
) : StatsFeatureComponent, ComponentContext by componentContext {

    override val uiState: StateFlow<StatsFeatureComponent.UiState>
        get() = MutableStateFlow(StatsFeatureComponent.UiState())
/**
 * Pads balances mapped to days of the year, to cover a continuous period, from today
 * TODO vico can maybe do this..?
 */
private fun Map<Int, Double>.mapBalanceWithDatePadding(periodLength: Int): Map<Int, Double> {
    val today = nowAsLocalDateTime().date
    val startDate = today.minusDays(periodLength - 1)

    // Get the starting balance by checking if there's days in the period before
    // the data starts
    var lastKnownBalance = 0.0
    if (isNotEmpty()) {
        val sortedKeys = keys.toSortedSet()
        val firstDayInPeriod = sortedKeys.first()
        val daysBefore =
            sortedKeys.filter { dayInPeriod -> dayInPeriod < startDate.dayOfYear }
        if (daysBefore.isNotEmpty()) {
            lastKnownBalance = this[daysBefore.last()] ?: 0.0
        } else if (firstDayInPeriod >= startDate.dayOfYear) {
            lastKnownBalance = 0.0
        }
    }

    val paddedMap = mutableMapOf<Int, Double>()
    // Pad out the dates for each day in the period
    var currentDate = startDate
    repeat(periodLength) {
        val dayOfYear = currentDate.dayOfYear
        if (containsKey(dayOfYear)) {
            this@mapBalanceWithDatePadding[dayOfYear]?.also { balance ->
                paddedMap[dayOfYear] = balance
                lastKnownBalance = balance
            }
        } else {
            paddedMap[dayOfYear] = lastKnownBalance
        }
        currentDate = currentDate.plus(DatePeriod(days = 1))
    }

    return paddedMap
}
