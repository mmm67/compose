package com.example.habittrackerapp

import com.example.habittrackerapp.data.Habit
import com.example.habittrackerapp.utilities.Constants


data class HabitState(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val type: String = "YesNo",
    val frequency: String = "",
    val targetValue: Int = 0,
    val isReminderEnabled: Boolean = false,
    val categoryId: Int = Constants.INVALID_CATEGORY_ID,
    val doneTime: String = "",
    val hitCount: Int = 0,
    val hitCountUpdatedTime: Long = 0,
    var habits: List<Habit> = emptyList(),
    var error: String? = null,
    var todayHabits: List<Habit> = emptyList()
)