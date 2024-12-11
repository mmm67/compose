package com.example.habittrackerapp.compose

import android.icu.util.Calendar

// Data class to represent each day item
data class DateItem(
    val dayName: String,
    val date: Int,
    val isToday: Boolean
) {
    companion object {
        fun getWeekDateItems(): List<DateItem> {
            // Days of the week
            val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

            // Get today's date and calculate the start of the week
            val calendar = Calendar.getInstance()
            val todayIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1

            // Move the calendar to the start of the current week (Sunday)
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

            // Create a list of DateItem objects for the current week
            return daysOfWeek.mapIndexed { index, dayName ->
                val date = calendar.get(Calendar.DAY_OF_MONTH)
                val isToday = index == todayIndex

                // Move to the next day in the week
                calendar.add(Calendar.DAY_OF_MONTH, 1)

                // Return a DateItem object
                DateItem(dayName, date, isToday)
            }
        }
    }
}
