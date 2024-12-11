package com.example.habittrackerapp.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromListOfLongs(value: List<Long>?): String {
        return value?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun toListOfLongs(value: String): List<Long> {
        return if (value.isEmpty()) {
            emptyList()
        } else {
            value.split(",").map { it.toLong() }
        }
    }
}
