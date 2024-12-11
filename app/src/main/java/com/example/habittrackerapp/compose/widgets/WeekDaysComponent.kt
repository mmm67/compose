package com.example.habittrackerapp.compose.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.habittrackerapp.compose.DateItem


@Composable
fun WeekDaysComponent() {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val dateItems = DateItem.getWeekDateItems()

    val todayColorStops = listOf(Color(0xff919bff), Color(0xff133a94))
    val colorStops = listOf(Color(0xff60696b), Color(0xff858e96))

    val rowHeight = when {
        screenHeight < 400.dp -> 60.dp
        screenHeight < 600.dp -> 80.dp
        else -> 100.dp
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(rowHeight),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        dateItems.forEachIndexed { _, item ->
            Card(
                modifier = Modifier
                    .padding(4.dp)
                    .weight(1f),
                shape = RoundedCornerShape(20.dp),
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Brush.verticalGradient(
                                if (item.isToday) {
                                    todayColorStops
                                } else {
                                    colorStops
                                }
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = item.dayName,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = item.date.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

