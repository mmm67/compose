package com.example.habittrackerapp.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Spa
import androidx.compose.ui.graphics.Color
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.habittrackerapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class DatabasePrePopulator @Inject constructor(
    private val categoryDaoProvider: Provider<CategoryDao>
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        CoroutineScope(SupervisorJob()).launch(Dispatchers.IO) {
            populateCategories()
        }
    }

    private suspend fun populateCategories() {
        // Insert initial categories
        categoryDaoProvider.get().insert(
            Category(
                name = "Fitness",
                iconName = CategoryIcon.FITNESS_CENTER.iconName,
                color = 0xffe9d022
            )
        )
        categoryDaoProvider.get().insert(
            Category(
                name = "Favorite",
                iconName = CategoryIcon.FAVORITE.iconName,
                color = 0xff919bff
            )
        )
        categoryDaoProvider.get().insert(
            Category(
                name = "Household",
                iconName = CategoryIcon.HOME.iconName,
                color = 0x95f9c3
            )
        )
        categoryDaoProvider.get().insert(
            Category(
                name = "Animal",
                iconName = CategoryIcon.PETS.iconName,
                color = 0xfffffbaf
            )
        )
        categoryDaoProvider.get().insert(
            Category(
                name = "Health",
                iconName = CategoryIcon.HEARING.iconName,
                color = 0xfff5c900
            )
        )
        categoryDaoProvider.get().insert(
            Category(
                name = "Learning",
                iconName = CategoryIcon.LANGUAGE.iconName,
                color = 0xffef745c
            )
        )
        categoryDaoProvider.get().insert(
            Category(
                name = "Self-Care",
                iconName = CategoryIcon.SPA.iconName,
                color = 0xff30c5d2
            )
        )
        categoryDaoProvider.get().insert(
            Category(
                name = "Travel",
                iconName = CategoryIcon.TRAIN.iconName,
                color = 0xffeed991
            )
        )
        categoryDaoProvider.get().insert(
            Category(
                name = "Home",
                iconName = CategoryIcon.HOME.iconName,
                color = 0xff87a3a3
            )
        )

        categoryDaoProvider.get().insert(
            Category(
                name = "Finance",
                iconName = CategoryIcon.MONEY.iconName,
                color = 0xfff9cdc3
            )
        )
    }
}

