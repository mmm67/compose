package com.example.habittrackerapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Serializable
@Entity(
    tableName = "habits",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "frequency")
    val frequency: String,
    @ColumnInfo(name = "targetValue")
    val targetValue: Int,
    @ColumnInfo(name = "isReminderEnabled")
    val isReminderEnabled: Boolean,
    @ColumnInfo(name = "categoryId")
    val categoryId: Int,
    @ColumnInfo(name = "hitCount")
    val hitCount: Int,
    @ColumnInfo(name = "doneTime")
    val doneTime: String,
    @ColumnInfo(name = "hitCountUpdated")
    val hitCountUpdated: Long
)