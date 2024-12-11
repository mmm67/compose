package com.example.habittrackerapp

data class CategoryState(
    val selectedCategory: SelectedCategoryState = SelectedCategoryState(),
    val categoryListState: CategoryListState = CategoryListState()
)
