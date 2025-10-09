package com.github.saikcaskey.stats.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.github.saikcaskey.pokertracker.database.PokerTrackerDatabase
import com.github.saikcaskey.pokertracker.domain.CoroutineDispatchers
import com.github.saikcaskey.pokertracker.domain.datasource.UserDataSource
import com.github.saikcaskey.pokertracker.domain.extensions.asInstantOrNull
import com.github.saikcaskey.pokertracker.domain.extensions.asLocalDateTime
import com.github.saikcaskey.pokertracker.domain.repository.StatsRepository
import com.github.saikcaskey.pokertracker.domain.util.nowAsLocalDateTime
import com.github.saikcaskey.stats.ext.flatMapWithUserId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus

class StatsRepositoryImpl(
    database: PokerTrackerDatabase,
    private val userDataSource: UserDataSource,
    private val coroutineDispatchers: CoroutineDispatchers,
) : StatsRepository {

    private val statsQueries = database.statsQueries

    override fun getDailyBalanceForPeriod(
        inLastDatePeriod: DatePeriod,
        fromDate: LocalDateTime?,
    ): Flow<Map<Int, Double>> {
        // Start on endDate and remove the period from before then for the start date
        val endDate = fromDate?.date ?: nowAsLocalDateTime().date
        val startDate = endDate.minus(inLastDatePeriod)
        return userDataSource.storedUser.flatMapWithUserId { userId ->
            statsQueries.getDailyBalance(
                userId = userId,
                startDate = startDate.toString(),
                endDate = endDate.toString()
            )
                .asFlow()
                .mapToList(coroutineDispatchers.io)
                .map { list ->
                    list.associate {
                        Pair(
                            it.date.asInstantOrNull()?.asLocalDateTime()?.dayOfYear ?: -1,
                            it.balance ?: 0.0
                        )
                    }
                }
        }
    }
}
