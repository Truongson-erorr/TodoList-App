package com.example.todo_app.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarView(
    datesWithNotes: Set<String>,
    onDateSelected: (String) -> Unit
) {
    val today = LocalDate.now()
    val currentYearMonth = YearMonth.now()
    val firstDayOfMonth = currentYearMonth.atDay(1)
    val daysInMonth = currentYearMonth.lengthOfMonth()

    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "${currentYearMonth.month.name} ${currentYearMonth.year}",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color.LightGray,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            val daysOfWeek = listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")
            daysOfWeek.forEach { day ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = day, color = Color.LightGray, fontSize = 14.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Lịch từng tuần (6 hàng max)
        var dayCounter = 1
        for (week in 1..6) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (dow in 1..7) {
                    if ((week == 1 && dow < firstDayOfWeek) || dayCounter > daysInMonth) {
                        // ô trống
                        Box(modifier = Modifier.weight(1f).aspectRatio(1f)) {}
                    } else {
                        val date = currentYearMonth.atDay(dayCounter)
                        val dateStr = date.format(formatter)
                        val hasNote = datesWithNotes.contains(dateStr)

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(4.dp)
                                .clickable { onDateSelected(dateStr) },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = dayCounter.toString(),
                                    color = if (date == today) Color.Red else Color.White
                                )
                                if (hasNote) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(Color.Red)
                                    )
                                }
                            }
                        }
                        dayCounter++
                    }
                }
            }
        }
    }
}
