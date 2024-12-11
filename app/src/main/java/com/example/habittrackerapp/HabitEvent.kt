package com.example.habittrackerapp

import com.example.habittrackerapp.data.Habit


sealed class HabitEvent {
    data object SaveHabit : HabitEvent()
    data class SetId(var id: Int) : HabitEvent()
    data class SetName(var name: String) : HabitEvent()
    data class SetDescription(var description: String) : HabitEvent()
    data class SetFrequency(var frequency: String) : HabitEvent()
    data class SetTargetValue(var targetValue: Int) : HabitEvent()
    data class SetType(var type: String) : HabitEvent()
    data class SetReminderEnabled(var enabled: Boolean) : HabitEvent()
    data class IncrementHitCount(val id: Int, val frequency: String) : HabitEvent()
    data class SetDoneTime(var doneTime: String) : HabitEvent()
    data class DeleteHabit(val habit: Habit) : HabitEvent()
    data object ResetSelectedHabit : HabitEvent()
}