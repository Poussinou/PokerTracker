package com.github.saikcaskey.pokertracker.domain.extensions

import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlin.time.Instant

fun LocalDateTime.toInstant(): Instant {
    return toInstant(TimeZone.currentSystemDefault())
}

fun LocalDateTime.plusMinutes(minutes: Int): Instant {
    return toInstant().plus(minutes, DateTimeUnit.MINUTE)
}

fun LocalDateTime.plusDays(days: Int): Instant {
    return toInstant().plus(DateTimePeriod(days = days), TimeZone.currentSystemDefault())
}
