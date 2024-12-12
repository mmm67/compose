package com.example.habittrackerapp

import com.example.habittrackerapp.data.Habit

data class HabitListState(
    var habits: List<Habit> = listOf(),
    var todayHabits: List<Habit> = listOf()
)