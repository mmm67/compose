package com.example.habittrackerapp


data class ReminderState(
    val id: Int = 0,
    val message: String = "",
    val habitId: Long = 0,
    val time: String = "Select Time",
    val date: String = "Select Date",
    val day: String = "Day"
)