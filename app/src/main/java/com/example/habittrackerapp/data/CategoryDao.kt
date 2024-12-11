package com.example.habittrackerapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("UPDATE categories SET habitCounts = habitCounts + 1 WHERE id = :categoryId")
    suspend fun incrementHabitCount(categoryId: Int)

    @Query("UPDATE categories SET habitCounts = habitCounts - 1 WHERE id = :categoryId")
    suspend fun decrementHabitCount(categoryId: Int)

    @Insert
    suspend fun insert(category: Category)

    @Delete
    suspend fun delete(category: Category)

    @Query("SELECT * FROM categories")
    fun getAllCategories() : Flow<List<Category>>

    @Query("SELECT id FROM categories WHERE name = :categoryName LIMIT 1")
    suspend fun getCategoryIdByName(categoryName: String): Int

    @Query("SELECT habitCounts FROM categories WHERE id = :categoryId")
    suspend fun getHabitCounts(categoryId: Int): Int

    @Query("SELECT COUNT(*) FROM categories WHERE LOWER(name) = LOWER(:categoryName)")
    suspend fun categoryExists(categoryName: String): Int
}