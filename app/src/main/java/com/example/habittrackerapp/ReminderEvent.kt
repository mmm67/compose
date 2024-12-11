package com.example.habittrackerapp


sealed class ReminderEvent {
    data class SetId(var id: Int) : ReminderEvent()
    data class SetMessage(var message: String) : ReminderEvent()
    data class SetDay(var day: String) : ReminderEvent()
    data class SetTime(var time: String) : ReminderEvent()
    data class SetDate(var date: String) : ReminderEvent()
    data class SetHabitId(var habitId: Long) : ReminderEvent()
    data object ResetSelectedReminder : ReminderEvent()
}