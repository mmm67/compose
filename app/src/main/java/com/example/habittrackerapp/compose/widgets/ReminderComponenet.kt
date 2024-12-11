package com.example.habittrackerapp.compose.widgets

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.example.habittrackerapp.utilities.Constants
import com.example.habittrackerapp.ReminderEvent
import com.example.habittrackerapp.ReminderState
import com.example.habittrackerapp.compose.dialogs.HabitDatePickerDialog
import com.example.habittrackerapp.compose.dialogs.HabitTimePickerDialog
import com.example.habittrackerapp.compose.menus.SimpleDropDownMenu
import com.example.habittrackerapp.utilities.formatTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderComponent(
    reminderState: ReminderState,
    onEvent: (ReminderEvent) -> Unit,
    frequency: String
) {

    var showTimePickerDialog by remember {
        mutableStateOf(false)
    }

    var showDatePickerDialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = reminderState.message,
            onValueChange = { onEvent(ReminderEvent.SetMessage(it)) },
            label = { Text("Remind message") },
            maxLines = 2,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xff919bff),
                unfocusedBorderColor = Color(0xff919bff)
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(30.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .border(
                        2.dp,
                        Color(0xff919bff),
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Button(shape = RectangleShape, colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ), onClick = { showTimePickerDialog = true }) {
                    AutoResizedText(text = reminderState.time, color = Color.Black)
                }
            }

            when (frequency) {
                Constants.HabitFrequency.DAILY -> {
                    onEvent(ReminderEvent.SetDay(Constants.HabitFrequency.DAILY))
                }

                Constants.HabitFrequency.WEEKLY -> {
                    SimpleDropDownMenu(
                        reminderState,
                        listOf(
                            "MONDAY",
                            "TUESDAY",
                            "WEDNESDAY",
                            "THURSDAY",
                            "FRIDAY",
                            "SATURDAY",
                            "SUNDAY"
                        )
                    ) {
                        onEvent(it)
                    }
                }

                Constants.HabitFrequency.MONTHLY -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .border(
                                2.dp,
                                Color(0xff919bff),
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        Button(shape = RectangleShape, colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ), onClick = { showDatePickerDialog = true }) {
                            AutoResizedText(text = reminderState.date, color = Color.Black)
                        }
                    }
                }
            }
        }
    }


    if (showTimePickerDialog) {
        HabitTimePickerDialog(
            onConfirm = {
                onEvent(ReminderEvent.SetTime(formatTime(it.hour, it.minute)))
                showTimePickerDialog = false
            },
            onDismiss = { showTimePickerDialog = false }
        )
    }

    if (showDatePickerDialog) {
        HabitDatePickerDialog(
            onDateSelected = {
                onEvent(ReminderEvent.SetDate(it))
                showDatePickerDialog = false
            },
            onDismiss = { showDatePickerDialog = false }
        )
    }
}