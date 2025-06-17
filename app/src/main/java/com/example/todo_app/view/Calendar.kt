package com.example.todo_app.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todo_app.viewmodel.NoteViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
    userId: String,
    navController: NavController,
    noteViewModel: NoteViewModel = viewModel()
) {
    LaunchedEffect(userId) {
        noteViewModel.loadNotes(userId)
    }

    val notes by noteViewModel.notes.collectAsState()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val today = LocalDate.now().format(formatter)

    var selectedDate by remember { mutableStateOf(today) }

    val datesWithNotes = remember(notes) {
        notes.mapNotNull {
            it.date.takeIf { dateStr ->
                try {
                    LocalDate.parse(dateStr.take(10), formatter)
                    true
                } catch (e: Exception) {
                    false
                }
            }?.take(10)
        }.toSet()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = 8.dp,
                backgroundColor = Color(0xFF2C2C2C)
            ) {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxSize()
                ) {
                    CalendarView(
                        datesWithNotes = datesWithNotes,
                        onDateSelected = { selectedDate = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Ghi chú ngày $selectedDate",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = Color.LightGray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            val filteredNotes = notes.filter { note ->
                note.date.take(10) == selectedDate
            }

            AnimatedVisibility(
                visible = filteredNotes.isEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = "Không có ghi chú cho ngày $selectedDate",
                    color = Color(0xFF6B7280),
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )
            }

            AnimatedVisibility(
                visible = filteredNotes.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(filteredNotes, key = { it.id }) { note ->
                        NoteCard(
                            note = note,
                            onEdit = { navController.navigate("EditNoteScreen/${note.id}/$userId") },
                            onDelete = { noteViewModel.deleteNote(note.id) },
                            onTogglePin = { id, pinned -> noteViewModel.togglePinNote(id, pinned) },
                            scale = 1f
                        )
                    }
                }
            }
        }
    }
}