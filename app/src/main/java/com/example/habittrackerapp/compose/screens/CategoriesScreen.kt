package com.example.habittrackerapp.compose.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.habittrackerapp.CategoryEvent
import com.example.habittrackerapp.CategoryListState
import com.example.habittrackerapp.OperationResult
import com.example.habittrackerapp.viewmodels.AppViewModel
import com.example.habittrackerapp.R
import com.example.habittrackerapp.compose.NavTopBar
import com.example.habittrackerapp.data.Category
import com.example.habittrackerapp.compose.Routes
import com.example.habittrackerapp.compose.dialogs.ColorPickerDialog
import com.example.habittrackerapp.compose.dialogs.IconPickerDialog
import com.example.habittrackerapp.data.CategoryIcon
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(viewModel: AppViewModel) {

    val categoryState = viewModel.categoryUiState.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState(false)
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }

    var selectedName by remember { mutableStateOf("") }
    var selectedIcon by rememberSaveable {
        mutableStateOf(Icons.Filled.QuestionMark.name)
    }
    var selectedColor by rememberSaveable {
        mutableLongStateOf(0xFFBBFFBBFF)
    }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.result.collect { result ->
            when (result) {
                is OperationResult.Error -> {
                    Toast.makeText(context, "Category already exists.", Toast.LENGTH_LONG).show()
                }

                is OperationResult.Success -> {
                }
            }
        }
    }
    fun showBottomSheet() {
        scope.launch {
            sheetState.show()
        }
    }

    // Function to hide the bottom sheet
    fun hideBottomSheet() {
        scope.launch {
            sheetState.hide()
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    openBottomSheet = true
                    showBottomSheet()
                },
                containerColor = Color(0xff919bff),
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add_category_icon),
                    contentDescription = "Add Category"
                )
            }
        }, topBar = {
            NavTopBar(title = {
                Text(text = Routes.Categories.route)
            }, canNavigateBack = false)
        }) { innerPadding ->
        CategoriesGridScreen(
            modifier = Modifier.padding(innerPadding),
            categoryState.value.categoryListState
        ) {
            viewModel.onEvent(it)
        }
    }


    if (openBottomSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                openBottomSheet = false
                hideBottomSheet()
            },
        ) {
            AddCategoryBottomSheet(
                selectedIcon,
                selectedColor,
                selectedName,
                onCategoryNameChanged = {
                    selectedName = it
                    viewModel.onEvent(CategoryEvent.SetSelectedCategoryName(it))
                },
                onColorChanged = {
                    selectedColor = it
                    viewModel.onEvent(CategoryEvent.SetSelectedCategoryColor(it))
                },
                onIconChanged = {
                    selectedIcon = it.iconName
                    viewModel.onEvent(CategoryEvent.SetSelectedCategoryIcon(it))
                },
                onAddCategory = {
                    openBottomSheet = false
                    hideBottomSheet()
                    viewModel.onEvent(CategoryEvent.AddCategory)
                    selectedIcon = Icons.Filled.QuestionMark.name
                    selectedColor = 0xFFBBFFBBFF
                    selectedName = ""
                }
            )
        }
    }
}

@Composable
fun AddCategoryBottomSheet(
    selectedIconName: String,
    selectedColor: Long,
    categoryName: String,
    onCategoryNameChanged: (String) -> Unit,
    onIconChanged: (CategoryIcon) -> Unit,
    onColorChanged: (Long) -> Unit,
    onAddCategory: () -> Unit
) {

    var openIconPickerDialog by rememberSaveable { mutableStateOf(false) }
    var openColorPickerDialog by rememberSaveable { mutableStateOf(false) }
    var showSupportingText by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
    ) {
        TextField(
            value = categoryName,
            onValueChange = {
                onCategoryNameChanged(it)
                showSupportingText = false
            },
            label = { Text("Category Name") },
            modifier = Modifier.fillMaxWidth(),
            supportingText = {
                if (showSupportingText) {
                    Text(text = "This filed is mandatory!", color = Color.Red)
                }
            },
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.mandatory),
                    contentDescription = "name",
                    tint = Color.Red
                )
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { openIconPickerDialog = true },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Icon:", style = MaterialTheme.typography.headlineSmall)
            Icon(
                imageVector = CategoryIcon.fromIconName(selectedIconName)?.imageVector
                    ?: Icons.Filled.QuestionMark,
                contentDescription = "Category Icon",
                modifier = Modifier
                    .size(36.dp)
                    .border(2.dp, Color.Black, CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { openColorPickerDialog = true },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Color:", style = MaterialTheme.typography.headlineSmall)
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(color = Color(selectedColor))
            )
        }


        Button(
            onClick = {
                if (categoryName.isNotEmpty()) {
                    onAddCategory()
                } else {
                    showSupportingText = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Add Category")
        }
    }

    if (openIconPickerDialog) {
        IconPickerDialog(onIconSelected = {
            onIconChanged(it)
        }) {
            openIconPickerDialog = false
        }
    }

    if (openColorPickerDialog) {
        ColorPickerDialog(onColorSelected = {
            onColorChanged(it)
        }) {
            openColorPickerDialog = false
        }
    }
}

@Composable
fun CategoriesGridScreen(
    modifier: Modifier = Modifier,
    categories: CategoryListState,
    onEvent: (CategoryEvent) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(120.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(categories.categories) { category ->
            CategoryCard(
                category = category,
                habitCount = category.habitCounts
            ) {
                onEvent(CategoryEvent.DeleteCategory(category))
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: Category,
    habitCount: Int,
    onDeleteCategory: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {


        Box(
            modifier = Modifier
                .background(
                    Color(category.color)
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icon at the top
                Image(
                    imageVector = CategoryIcon.fromIconName(category.iconName)?.imageVector
                        ?: Icons.Filled.QuestionMark,
                    contentDescription = category.name,
                    modifier = Modifier
                        .size(20.dp)
                )

                Text(
                    text = category.name,
                    style = MaterialTheme.typography.bodyMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$habitCount habits",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Icon(
                        Icons.Filled.Delete,
                        tint = Color.Red,
                        contentDescription = "Delete category",
                        modifier = Modifier
                            .clickable {
                                onDeleteCategory()
                            })
                }

            }
        }
    }
}

