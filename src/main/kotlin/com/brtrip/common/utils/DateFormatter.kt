package com.brtrip.common.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.yyyy_MM_dd_Formatter() =
    LocalDate.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd"))

fun String.yyyy_MM_dd_HH_mm_SS_Formatter() =
    LocalDateTime.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

fun LocalDate.format_yyyy_MM_dd(): String =
    this.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

fun LocalDateTime?.format_yyyy_MM_dd_hh_mm_ss(): String? =
    this?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
