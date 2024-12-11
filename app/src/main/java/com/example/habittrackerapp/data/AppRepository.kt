package com.example.habittrackerapp.data

import kotlinx.coroutines.flow.Flow

interface AppRepository {
    suspend fun addHabit(habit: Habit)

    suspend fun addCategory(category: Category)

    suspend fun addHabit(habit: Habit, reminder: Reminder)

    fun getAllHabits(): Flow<List<Habit>>

    suspend fun deleteHabit(habit: Habit)

    fun getHabitsWithTodayReminders(todayDayOfWeek: String, todayDate: String): Flow<List<Habit>>

    fun getHabits(frequency: String): Flow<List<Habit>>

    suspend fun incrementHitCount(habitId: Int, frequency: String)

    fun getAllCategories(): Flow<List<Category>>

    suspend fun getCategory(categoryName: String): Int?

    suspend fun deleteCategory(category: Category)

    suspend fun categoryExists(name: String): Int

    suspend fun getHabitsByCategoryId(categoryId: Int): List<Habit>
}