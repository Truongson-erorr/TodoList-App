package com.example.todo_app.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todo_app.viewmodel.NoteViewModel

@Composable
fun NoteByDateScreen(
    userId: String,
    selectedDate: String,
    navController: NavController,
    noteViewModel: NoteViewModel = viewModel()
) {
    LaunchedEffect(userId) {
        if (userId.isNotBlank()) {
            noteViewModel.loadNotes(userId)
        }
    }

    val notes by noteViewModel.notes.collectAsState()

    // Lọc ghi chú theo ngày, cắt 10 ký tự đầu yyyy-MM-dd để so sánh chính xác
    val filteredNotes = notes.filter { note ->
        note.date.take(10) == selectedDate
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(
            text = "Ghi chú ngày $selectedDate",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredNotes.isEmpty()) {
            Text(text = "Không có ghi chú ngày này")
        } else {
            LazyColumn {
                items(filteredNotes) { note ->
                    NoteCard(
                        note = note,
                        onEdit = { navController.navigate("EditNoteScreen/${note.id}/$userId") },
                        onDelete = { /* Xử lý xóa */ },
                        onTogglePin = { id, pinned -> noteViewModel.togglePinNote(id, pinned) },
                        scale = 1f
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
