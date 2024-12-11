package com.example.habittrackerapp.utilities

object Constants {

    const val INVALID_CATEGORY_ID = -1
    const val DATABASE_NAME = "app_database"

    object HabitFrequency {
        const val DAILY = "daily"
        const val WEEKLY = "weekly"
        const val MONTHLY = "monthly"
    }

    object NotificationChannelProperties {
        const val CHANNEL_ID = "habit_reminder_channel"
        const val CHANNEL_NAME = "Habit Reminders"
        const val CHANNEL_DESCRIPTION ="Channel for habit reminders"

    }

    object NotificationWorkerParams{
        const val HABIT_NAME = "habit_name"
        const val REMINDER_MESSAGE = "reminder_message"
    }
}