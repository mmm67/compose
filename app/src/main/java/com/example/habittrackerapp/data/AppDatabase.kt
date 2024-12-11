package com.example.habittrackerapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.habittrackerapp.utilities.Constants
import javax.inject.Provider

@Database(
    entities = [(Category::class), (Habit::class), (Reminder::class)],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun habitDao(): HabitDao
    abstract fun reminderDao(): ReminderDao
    abstract fun categoryDao(): CategoryDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context, provider: Provider<CategoryDao>): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context, provider).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context, provider: Provider<CategoryDao>): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "${Constants.DATABASE_NAME}.db"
            ).addCallback(
                DatabasePrePopulator(categoryDaoProvider = provider)
            ).build()
        }
    }
}
