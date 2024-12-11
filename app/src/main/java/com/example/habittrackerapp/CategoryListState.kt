package com.example.habittrackerapp

import com.example.habittrackerapp.data.Category
import com.example.habittrackerapp.data.CategoryIcon

data class CategoryListState(
    val categories: List<Category> = emptyList()
)