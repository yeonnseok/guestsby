package com.brtrip.common.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.yyyy_MM_dd_Formatter() =
    LocalDate.parse(this, DateTimeFormatter.ISO_DATE)

fun String.yyyy_MM_dd_HH_mm_SS_Formatter() =
    LocalDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
