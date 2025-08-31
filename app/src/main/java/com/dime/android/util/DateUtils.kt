package com.dime.android.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val dayFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
    private val fullDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    private val monthFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())
    
    fun formatDay(date: Date): String = dayFormat.format(date)
    fun formatFullDate(date: Date): String = fullDateFormat.format(date)
    fun formatMonth(date: Date): String = monthFormat.format(date)
    
    fun startOfDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }
    
    fun endOfDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.time
    }
    
    fun startOfWeek(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        return startOfDay(calendar.time)
    }
    
    fun endOfWeek(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = startOfWeek(date)
        calendar.add(Calendar.DAY_OF_WEEK, 6)
        return endOfDay(calendar.time)
    }
    
    fun startOfMonth(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        return startOfDay(calendar.time)
    }
    
    fun endOfMonth(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        return endOfDay(calendar.time)
    }
    
    fun startOfYear(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.DAY_OF_YEAR, 1)
        return startOfDay(calendar.time)
    }
    
    fun endOfYear(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR))
        return endOfDay(calendar.time)
    }
}
