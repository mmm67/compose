package com.example.habittrackerapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Insert
    suspend fun insert(habit: Habit): Long

    @Delete
    suspend fun delete(habit: Habit)

    @Query("SELECT * FROM habits ORDER BY name ASC")
    fun getAllHabits(): Flow<List<Habit>>

    @Transaction
    @Query("""
        SELECT * FROM habits
        WHERE id IN (
            SELECT habitId FROM reminders
            WHERE day = :todayDayOfWeek  -- Weekly habits 
            OR day = "daily" -- Daily habits
            OR (strftime('%d', day) = strftime('%d', :todayDate))  -- Monthly habits 
        )
    """)
    fun getHabitsWithTodayReminders(todayDayOfWeek: String, todayDate: String): Flow<List<Habit>>

    @Query("SELECT * FROM habits WHERE frequency = :frequency")
    fun getHabits(frequency: String): Flow<List<Habit>>

    @Transaction
    @Query("""
        UPDATE habits
        SET hitCount = hitCount + 1, hitCountUpdated = :currentTime
        WHERE id = :habitId
        AND (
            (:frequency = 'daily' AND hitCountUpdated <= :timeThresholdDaily) OR
            (:frequency = 'weekly' AND hitCountUpdated <= :timeThresholdWeekly) OR
            (:frequency = 'monthly' AND hitCountUpdated <= :timeThresholdMonthly)
        )
    """)
    suspend fun incrementHitCount(
        habitId: Int,
        frequency: String,
        currentTime: Long,
        timeThresholdDaily: Long,
        timeThresholdWeekly: Long,
        timeThresholdMonthly: Long
    )

    @Query("SELECT * FROM habits WHERE categoryId = :categoryId")
    suspend fun getHabitsByCategoryId(categoryId: Int): List<Habit>
}