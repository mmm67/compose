package com.example.habittrackerapp.data

import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao,
    private val reminderDao: ReminderDao,
    private val categoryDao: CategoryDao,
    private val transactionProvider: TransactionProvider
) : AppRepository{

    override suspend fun addHabit(habit: Habit) {
        transactionProvider.runAsTransaction {
            habitDao.insert(habit)
            categoryDao.incrementHabitCount(habit.categoryId)
        }
    }

    override suspend fun addCategory(category: Category) {
        categoryDao.insert(category)
    }

    override suspend fun addHabit(habit: Habit, reminder: Reminder) {
        transactionProvider.runAsTransaction {
            val id = habitDao.insert(habit)
            reminder.habitId = id
            reminderDao.insert(reminder)
            categoryDao.incrementHabitCount(habit.categoryId)
        }
    }

    override fun getAllHabits(): Flow<List<Habit>> {
        return habitDao.getAllHabits()
    }

    override suspend fun deleteHabit(habit: Habit) {
        transactionProvider.runAsTransaction {
            habitDao.delete(habit)
            categoryDao.decrementHabitCount(habit.categoryId)
        }

    }

    override fun getHabitsWithTodayReminders(todayDayOfWeek: String, todayDate: String): Flow<List<Habit>> {
        return habitDao.getHabitsWithTodayReminders(todayDayOfWeek, todayDate)
    }

    override fun getHabits(frequency: String) = habitDao.getHabits(frequency)

    override suspend fun incrementHitCount(habitId: Int, frequency: String) {
        val currentTime = System.currentTimeMillis()

        val timeThresholdDaily = currentTime - TimeUnit.DAYS.toMillis(1)
        val timeThresholdWeekly = currentTime - TimeUnit.DAYS.toMillis(7)
        val timeThresholdMonthly = currentTime - TimeUnit.DAYS.toMillis(30)

        habitDao.incrementHitCount(
            habitId = habitId,
            frequency = frequency,
            currentTime = currentTime,
            timeThresholdDaily = timeThresholdDaily,
            timeThresholdWeekly = timeThresholdWeekly,
            timeThresholdMonthly = timeThresholdMonthly
        )
    }

    override fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories()
    }

    override suspend fun getCategory(categoryName: String): Int {
        return categoryDao.getCategoryIdByName(categoryName.trim())
    }

    override suspend fun categoryExists(name: String): Int {
        return categoryDao.categoryExists(name)
    }

    override suspend fun deleteCategory(category: Category) {
        return categoryDao.delete(category)
    }

    override suspend fun getHabitsByCategoryId(categoryId: Int): List<Habit> {
        return habitDao.getHabitsByCategoryId(categoryId)
    }
}