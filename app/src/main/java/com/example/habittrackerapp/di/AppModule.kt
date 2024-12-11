package com.example.habittrackerapp.di

import android.content.Context
import androidx.work.WorkManager
import com.example.habittrackerapp.data.AppDatabase
import com.example.habittrackerapp.data.AppRepositoryImpl
import com.example.habittrackerapp.data.CategoryDao
import com.example.habittrackerapp.data.HabitDao
import com.example.habittrackerapp.data.ReminderDao
import com.example.habittrackerapp.data.TransactionProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        provider: Provider<CategoryDao>,
    ) = AppDatabase.getInstance(context, provider)

    @Provides
    fun provideCategoryDao(appDatabase: AppDatabase) = appDatabase.categoryDao()

    @Provides
    fun provideHabitDao(appDatabase: AppDatabase) = appDatabase.habitDao()

    @Provides
    fun provideReminderDao(appDatabase: AppDatabase) = appDatabase.reminderDao()

    @Provides
    fun provideTransactionProvider(appDatabase: AppDatabase) = TransactionProvider(appDatabase)

    @Provides
    @Singleton
    fun provideAppRepository(
        habitDao: HabitDao,
        reminderDao: ReminderDao,
        categoryDao: CategoryDao,
        transactionProvider: TransactionProvider
    ) = AppRepositoryImpl(habitDao, reminderDao, categoryDao, transactionProvider)

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
}
