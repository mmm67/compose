package com.example.habittrackerapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.habittrackerapp.CategoryEvent
import com.example.habittrackerapp.CategoryState
import com.example.habittrackerapp.SelectedCategoryState
import com.example.habittrackerapp.HabitEvent
import com.example.habittrackerapp.HabitState
import com.example.habittrackerapp.OperationResult
import com.example.habittrackerapp.ReminderEvent
import com.example.habittrackerapp.ReminderState
import com.example.habittrackerapp.data.Habit
import com.example.habittrackerapp.data.Reminder
import com.example.habittrackerapp.data.AppRepositoryImpl
import com.example.habittrackerapp.data.Category
import com.example.habittrackerapp.utilities.Constants
import com.example.habittrackerapp.utilities.Constants.NotificationWorkerParams.HABIT_NAME
import com.example.habittrackerapp.utilities.Constants.NotificationWorkerParams.REMINDER_MESSAGE
import com.example.habittrackerapp.worker.NotificationWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class AppViewModel @Inject constructor(
    private val repository: AppRepositoryImpl,
    private val workManager: WorkManager,
) : ViewModel() {

    private val _categoryUiState = MutableStateFlow(CategoryState())
    val categoryUiState: StateFlow<CategoryState> = _categoryUiState.asStateFlow()

    private val _habitState = MutableStateFlow(HabitState())
    val habitState: StateFlow<HabitState> = _habitState.asStateFlow()

    private val _reminderState = MutableStateFlow(ReminderState())
    val reminderState: StateFlow<ReminderState> = _reminderState.asStateFlow()

    private val _result = MutableSharedFlow<OperationResult>()
    val result: SharedFlow<OperationResult> = _result

    val sortedHabits: StateFlow<List<Habit>> = _habitState
        .map { habitState -> habitState.habits.sortedBy { it.categoryId } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val sortedTodayHabits: StateFlow<List<Habit>> = _habitState
        .map { habitState -> habitState.todayHabits.sortedBy { it.categoryId } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categoryMap: StateFlow<Map<Int, Category>> = _categoryUiState
        .map { it.categoryListState.categories.associateBy { category -> category.id } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    init {
        getAllHabits()
        getAllCategories()
        loadTodayHabits()
    }

    private fun getAllHabits() {
        viewModelScope.launch {
            repository.getAllHabits().collectLatest { habits ->
                _habitState.update {
                    it.copy(habits = habits)
                }
            }
        }
    }

    private fun getAllCategories() {
        viewModelScope.launch {
            repository.getAllCategories().collectLatest { categories ->
                _categoryUiState.value = _categoryUiState.value.copy(
                    categoryListState = _categoryUiState.value.categoryListState.copy(
                        categories = categories
                    )
                )
            }
        }
    }

    private fun loadTodayHabits() {
        viewModelScope.launch {
            val todayDayOfWeek = LocalDate.now().dayOfWeek.name
            val todayDate = LocalDate.now().toString()

            val habitsWithReminders =
                repository.getHabitsWithTodayReminders(todayDayOfWeek, todayDate)
            val dailyHabits = repository.getHabits(Constants.HabitFrequency.DAILY)

            combine(
                habitsWithReminders,
                dailyHabits
            ) { _habitsWithReminders, _dailyHabits ->
                _habitsWithReminders to _dailyHabits
            }.collectLatest { (habitsWithReminders, dailyHabits) ->
                _habitState.update {
                    it.copy(todayHabits = (habitsWithReminders + dailyHabits).distinct())
                }
            }

        }
    }


    fun onEvent(event: HabitEvent) {
        when (event) {
            HabitEvent.SaveHabit -> {
                viewModelScope.launch {
                    _habitState.update {
                        it.copy(categoryId = repository.getCategory(_categoryUiState.value.selectedCategory.name))
                    }

                    if (_habitState.value.name.isEmpty() || _habitState.value.categoryId == Constants.INVALID_CATEGORY_ID) {
                        _result.emit(OperationResult.Error("Fill out mandatory items."))
                        return@launch
                    }

                    val habit = Habit(
                        name = _habitState.value.name,
                        description = _habitState.value.description,
                        type = _habitState.value.type,
                        frequency = _habitState.value.frequency,
                        isReminderEnabled = _habitState.value.isReminderEnabled,
                        categoryId = _habitState.value.categoryId,
                        hitCount = _habitState.value.hitCount,
                        doneTime = _habitState.value.doneTime,
                        targetValue = _habitState.value.targetValue,
                        hitCountUpdated = _habitState.value.hitCountUpdatedTime
                    )

                    if (_habitState.value.isReminderEnabled) {
                        if (_habitState.value.frequency == Constants.HabitFrequency.DAILY && _reminderState.value.time == "Select Time") {
                            _result.emit(OperationResult.Error("Fill out Time."))
                            return@launch
                        }
                        if (_habitState.value.frequency == Constants.HabitFrequency.MONTHLY && _reminderState.value.date == "Select Date") {
                            _result.emit(OperationResult.Error("Fill out Date."))
                            return@launch
                        }
                        if (_habitState.value.frequency == Constants.HabitFrequency.WEEKLY && _reminderState.value.day == "Day") {
                            _result.emit(OperationResult.Error("Fill out Day."))
                            return@launch
                        }

                        val reminder = Reminder(
                            time = _reminderState.value.time,
                            day = _reminderState.value.day,
                            date = _reminderState.value.date,
                            message = _reminderState.value.message
                        )
                        repository.addHabit(habit = habit, reminder)
                        scheduleReminder(
                            _habitState.value.id,
                            _habitState.value.name,
                            _reminderState.value.message
                        )

                    } else {
                        repository.addHabit(habit = habit)
                    }

                    _result.emit(OperationResult.Success)
                    _habitState.value = HabitState()
                }
            }

            is HabitEvent.SetDescription -> {
                _habitState.update {
                    it.copy(description = event.description)
                }
            }

            is HabitEvent.SetFrequency -> {
                _habitState.update {
                    it.copy(frequency = event.frequency)
                }
            }

            is HabitEvent.SetId -> {
                _habitState.update {
                    it.copy(id = event.id)
                }
            }

            is HabitEvent.SetName -> {
                _habitState.update {
                    it.copy(name = event.name)
                }
            }

            is HabitEvent.SetReminderEnabled -> {
                _habitState.update {
                    it.copy(isReminderEnabled = event.enabled)
                }
            }

            is HabitEvent.SetTargetValue -> {
                _habitState.update {
                    it.copy(targetValue = event.targetValue)
                }
            }

            is HabitEvent.SetType -> {
                _habitState.update {
                    it.copy(type = event.type)
                }
            }

            is HabitEvent.SetDoneTime -> {}
            is HabitEvent.DeleteHabit -> {
                viewModelScope.launch {
                    if (event.habit.isReminderEnabled) {
                        workManager.cancelAllWorkByTag("habit_${event.habit.id}")
                    }
                    repository.deleteHabit(event.habit)
                }
            }

            is HabitEvent.IncrementHitCount -> {
                viewModelScope.launch {
                    repository.incrementHitCount(event.id, event.frequency)
                }
            }

            HabitEvent.ResetSelectedHabit -> {
                _habitState.value = HabitState()
            }
        }
    }

    fun onEvent(event: ReminderEvent) {
        when (event) {
            is ReminderEvent.SetHabitId -> {
                _reminderState.update {
                    it.copy(habitId = event.habitId)
                }
            }

            is ReminderEvent.SetDate -> {
                _reminderState.update {
                    it.copy(date = event.date)
                }
            }

            is ReminderEvent.SetDay -> {
                _reminderState.update {
                    it.copy(day = event.day)
                }
            }

            is ReminderEvent.SetId -> {
                _reminderState.update {
                    it.copy(id = event.id)
                }
            }

            is ReminderEvent.SetMessage -> {
                _reminderState.update {
                    it.copy(message = event.message)
                }
            }

            is ReminderEvent.SetTime -> {
                _reminderState.update {
                    it.copy(time = event.time)
                }
            }

            ReminderEvent.ResetSelectedReminder -> {
                _reminderState.value = ReminderState()
            }
        }
    }

    fun onEvent(event: CategoryEvent) {
        when (event) {
            CategoryEvent.AddCategory -> {
                viewModelScope.launch {
                    val categoryExists =
                        repository.categoryExists(_categoryUiState.value.selectedCategory.name)
                    if (categoryExists != 0) {
                        _result.emit(OperationResult.Error("Category ${_categoryUiState.value.selectedCategory.name} already exists."))
                        return@launch
                    }

                    repository.addCategory(
                        Category(
                            name = _categoryUiState.value.selectedCategory.name,
                            iconName = _categoryUiState.value.selectedCategory.categoryIcon.iconName,
                            color = _categoryUiState.value.selectedCategory.colorId
                        )
                    )
                }
            }

            is CategoryEvent.SetSelectedCategoryColor -> {
                _categoryUiState.value = _categoryUiState.value.copy(
                    selectedCategory = _categoryUiState.value.selectedCategory.copy(
                        colorId = event.color
                    )
                )
            }

            is CategoryEvent.SetSelectedCategoryIcon -> {
                _categoryUiState.value = _categoryUiState.value.copy(
                    selectedCategory = _categoryUiState.value.selectedCategory.copy(
                        categoryIcon = event.categoryIcon
                    )
                )
            }

            is CategoryEvent.SetSelectedCategoryName -> {
                _categoryUiState.value = _categoryUiState.value.copy(
                    selectedCategory = _categoryUiState.value.selectedCategory.copy(
                        name = event.name
                    )
                )
            }

            is CategoryEvent.DeleteCategory -> {
                viewModelScope.launch {
                    // Cancel all reminders for habits in deleted category
                    repository.getHabitsByCategoryId(event.category.id).forEach { habit ->
                        if (habit.isReminderEnabled) {
                            workManager.cancelAllWorkByTag("habit_${habit.id}")
                        }
                    }

                    repository.deleteCategory(event.category)
                }
            }

            CategoryEvent.ResetSelectedCategory -> {
                _categoryUiState.value = _categoryUiState.value.copy(
                    selectedCategory = SelectedCategoryState() // Reset to default
                )
            }
        }
    }

    private fun scheduleReminder(
        habitId: Int,
        habitName: String,
        message: String
    ) {
        fun dayStringToInt(day: String): Int? {
            return when (day.lowercase()) {
                "sunday" -> Calendar.SUNDAY
                "monday" -> Calendar.MONDAY
                "tuesday" -> Calendar.TUESDAY
                "wednesday" -> Calendar.WEDNESDAY
                "thursday" -> Calendar.THURSDAY
                "friday" -> Calendar.FRIDAY
                "saturday" -> Calendar.SATURDAY
                else -> null
            }
        }

        fun parseDate(dateString: String): Calendar {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = dateFormat.parse(dateString)
            val calendar = Calendar.getInstance()
            if (date != null) {
                calendar.time = date
            }

            return calendar
        }

        fun getReminderTime(): Long {
            var reminderTime = 0L
            val timeParts = if (reminderState.value.time == "Select Time") {
                "12:00"
            } else {
                reminderState.value.time
            }.split(":")
            val hour = timeParts[0].toInt()
            val minute = timeParts[1].toInt()

            var calendar = Calendar.getInstance()

            when (habitState.value.frequency) {
                Constants.HabitFrequency.DAILY -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)
                    calendar.set(Calendar.SECOND, 0)

                    if (calendar.timeInMillis < System.currentTimeMillis()) {
                        calendar.add(Calendar.DAY_OF_YEAR, 1) // Schedule for the next day
                    }

                    reminderTime = calendar.timeInMillis
                }

                Constants.HabitFrequency.WEEKLY -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)
                    calendar.set(Calendar.SECOND, 0)

                    // Get the current day of the week
                    val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                    val selectedDayOfWeek = dayStringToInt(reminderState.value.day)

                    val daysToAdd = if (selectedDayOfWeek!! >= currentDayOfWeek) {
                        selectedDayOfWeek - currentDayOfWeek // If it's later this week
                    } else {
                        // If it's earlier this week, schedule for next week
                        7 - currentDayOfWeek + selectedDayOfWeek
                    }

                    // Add the necessary days to the current date to get the correct day for the reminder
                    calendar.add(Calendar.DAY_OF_YEAR, daysToAdd)

                    if (calendar.timeInMillis < System.currentTimeMillis()) {
                        calendar.add(
                            Calendar.WEEK_OF_YEAR,
                            1
                        ) // If reminder time is in the past, set for next week
                    }

                    reminderTime = calendar.timeInMillis
                }

                Constants.HabitFrequency.MONTHLY -> {
                    calendar = parseDate(reminderState.value.date)
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)
                    calendar.set(Calendar.SECOND, 0)

                    if (calendar.timeInMillis < System.currentTimeMillis()) {
                        calendar.add(
                            Calendar.MONTH,
                            1
                        ) // If the date is in the past, schedule for next month
                    }

                    reminderTime = calendar.timeInMillis
                }
            }

            return reminderTime
        }


        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(getReminderTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
            .setInputData(workDataOf(HABIT_NAME to habitName))
            .setInputData(workDataOf(REMINDER_MESSAGE to message))
            .setTraceTag("habit_${habitId}")
            .build()

        workManager.enqueue(workRequest)
    }
}