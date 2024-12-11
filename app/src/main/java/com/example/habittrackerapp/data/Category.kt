package com.example.habittrackerapp.data


import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "categories")
data class Category (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "name")
    var name: String = "Sport",

    @ColumnInfo(name = "habitCounts")
    var habitCounts: Int = 0,

    @ColumnInfo(name = "iconName")
    var iconName: String = "",

    @ColumnInfo(name = "color")
    @TypeConverters(Converters::class)
    var color: Long = 0xffe9d022,
)