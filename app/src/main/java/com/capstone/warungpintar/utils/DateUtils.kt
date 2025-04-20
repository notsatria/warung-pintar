package com.capstone.warungpintar.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val locale = Locale("in", "ID")

fun Long.toStrDate(outputFormat: String = "dd/MM/yyyy"): String {
    val format = SimpleDateFormat(outputFormat, locale)
    val date = java.util.Date(this)

    return format.format(date)
}

fun String.toMillis(inputFormat: String = "dd/MM/yyyy"): Long {
    val format = SimpleDateFormat(inputFormat, locale)
    val date = format.parse(this) as Date

    return date.time
}