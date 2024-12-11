package com.example.habittrackerapp

import com.example.habittrackerapp.data.Category
import com.example.habittrackerapp.data.CategoryIcon

sealed class CategoryEvent {
    data class SetSelectedCategoryName(val name: String) : CategoryEvent()
    data class SetSelectedCategoryColor(val color: Long) : CategoryEvent()
    data class SetSelectedCategoryIcon(val categoryIcon: CategoryIcon) : CategoryEvent()
    data object AddCategory : CategoryEvent()
    data class DeleteCategory(val category: Category) : CategoryEvent()
    data object ResetSelectedCategory : CategoryEvent()
}

