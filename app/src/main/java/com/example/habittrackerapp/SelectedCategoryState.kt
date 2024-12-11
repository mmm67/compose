package com.example.habittrackerapp

import com.example.habittrackerapp.data.Category
import com.example.habittrackerapp.data.CategoryIcon

data class SelectedCategoryState(
    val name: String = "",
    val categoryIcon: CategoryIcon = CategoryIcon.SPA,
    val colorId: Long = 0xFF4CAF50,
    val categories: List<Category> = emptyList(),
    val habitCounts: Int = 0
)