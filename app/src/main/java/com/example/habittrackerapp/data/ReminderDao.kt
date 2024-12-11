package com.example.habittrackerapp.data

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface ReminderDao {

    @Insert
    suspend fun insert(reminder: Reminder)
}