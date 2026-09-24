package ru.artem_torpedo.thechronicle.presentation.utils

import android.icu.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val formatter = SimpleDateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())

fun Long.convertToDate(): String = formatter.format(Date(this))