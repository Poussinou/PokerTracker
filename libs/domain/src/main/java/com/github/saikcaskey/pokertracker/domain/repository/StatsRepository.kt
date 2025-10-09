package com.github.saikcaskey.pokertracker.domain.repository

import com.github.saikcaskey.pokertracker.domain.util.nowAsLocalDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDateTime

interface StatsRepository {

    fun getDailyBalanceForPeriod(
        inLastDatePeriod: DatePeriod,
        fromDate: LocalDateTime? = nowAsLocalDateTime(),
    ): Flow<Map<Int, Double>>
}
