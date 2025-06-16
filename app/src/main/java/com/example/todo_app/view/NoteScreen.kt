package com.example.todo_app.view

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todo_app.model.Note
import com.example.todo_app.viewmodel.NoteViewModel

@Composable
fun NoteScreen(
    userId: String,
    navController: NavController,
    noteViewModel: NoteViewModel = viewModel()
) {
    val notes by noteViewModel.notes.collectAsState()
    val context = LocalContext.current
    val config = LocalConfiguration.current
    val screenScale = config.screenWidthDp / 360f

    var showDialog by remember { mutableStateOf(false) }
    var selectedNote by remember { mutableStateOf<Note?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Tất cả") }
    val categories = listOf("Tất cả", "Work", "Home", "Healthy")

    LaunchedEffect(userId) {
        noteViewModel.loadNotes(userId)
    }

    val filteredNotes = notes
        .filter { note ->
            (searchQuery.isEmpty() || note.title.contains(searchQuery, ignoreCase = true)) &&
                    (selectedCategory == "Tất cả" || note.category == selectedCategory)
        }
        .sortedByDescending { it.isPinned }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = (16 * screenScale).dp, vertical = (16 * screenScale).dp)
        ) {
            Spacer(modifier = Modifier.height((10 * screenScale).dp))
            Text(
                text = "Ghi chú của bạn",
                fontSize = (22 * screenScale).sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6200EE),
                modifier = Modifier.padding(bottom = (8 * screenScale).dp)
            )

            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height((70 * screenScale).dp)
                    .padding(bottom = (12 * screenScale).dp)
                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape((12 * screenScale).dp)),
                placeholder = { Text("Tìm kiếm theo tên ghi chú...", fontSize = 12.sp) },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Tìm kiếm",
                        tint = Color.Gray
                    )
                },
                shape = RoundedCornerShape((12 * screenScale).dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0xFFF5F5F5),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = (8 * screenScale).dp),
                horizontalArrangement = Arrangement.spacedBy((8 * screenScale).dp)
            ) {
                categories.forEach { category ->
                    val isSelected = selectedCategory == category
                    Button(
                        onClick = { selectedCategory = category },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (isSelected) Color(0xFF6200EE) else Color(0xFFE0E0E0),
                            contentColor = if (isSelected) Color.White else Color.Black
                        ),
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(horizontal = (12 * screenScale).dp, vertical = (6 * screenScale).dp),
                        modifier = Modifier.defaultMinSize(minHeight = 0.dp, minWidth = 0.dp)
                    ) {
                        Text(
                            text = category,
                            fontSize = (8 * screenScale).sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height((8 * screenScale).dp))

            if (filteredNotes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription = "Không có ghi chú",
                            tint = Color.LightGray,
                            modifier = Modifier.size((48 * screenScale).dp)
                        )
                        Spacer(modifier = Modifier.height((8 * screenScale).dp))
                        Text(
                            text = "Không tìm thấy ghi chú",
                            fontSize = (16 * screenScale).sp,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                LazyColumn {
                    items(filteredNotes) { note ->
                        NoteCard(
                            note = note,
                            onEdit = { navController.navigate("EditNoteScreen/${note.id}/$userId") },
                            onDelete = {
                                selectedNote = note
                                showDialog = true
                            },
                            onTogglePin = { noteId, isPinned ->
                                noteViewModel.togglePinNote(noteId, isPinned)
                            },
                            scale = screenScale
                        )
                        Spacer(modifier = Modifier.height((6 * screenScale).dp))
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate("AddNoteScreen/$userId") },
            backgroundColor = Color(0xFF6200EE),
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding((10 * screenScale).dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Thêm ghi chú")
        }
    }

    if (showDialog && selectedNote != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Xác nhận xóa") },
            text = { Text("Bạn có chắc chắn muốn xóa ghi chú này không?") },
            confirmButton = {
                TextButton(onClick = {
                    selectedNote?.let {
                        noteViewModel.deleteNote(it.id)
                        Toast.makeText(context, "Đã xóa ghi chú", Toast.LENGTH_SHORT).show()
                    }
                    showDialog = false
                }) {
                    Text("Xóa", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Hủy", color = Color.Black)
                }
            }
        )
    }
}

@Composable
fun NoteCard(
    note: Note,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onTogglePin: (noteId: String, isPinned: Boolean) -> Unit,
    scale: Float
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val categoryBackgroundColor = when (note.category) {
        "Work" -> Color(0xFFE3F2FD)
        "Home" -> Color(0xFFE8F5E9)
        "Study" -> Color(0xFFFFF3E0)
        "Healthy" -> Color(0xFFFFEBEE)
        else -> Color(0xFFF0F0F0)
    }
    val categoryTextColor = when (note.category) {
        "Work" -> Color(0xFF1976D2)
        "Home" -> Color(0xFF388E3C)
        "Study" -> Color(0xFFF57C00)
        "Healthy" -> Color(0xFFD81B60)
        else -> Color(0xFF424242)
    }

    Card(
        shape = RoundedCornerShape((10 * scale).dp),
        elevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding((10 * scale).dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = note.title,
                            fontSize = (12 * scale).sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colors.primaryVariant
                        )
                        if (note.isPinned) {
                            Spacer(modifier = Modifier.width((4 * scale).dp))
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Ghim",
                                tint = Color.Red,
                                modifier = Modifier.size((16 * scale).dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height((4 * scale).dp))
                    Text(
                        text = note.description,
                        fontSize = (10 * scale).sp,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height((2 * scale).dp))
                    Text(
                        text = note.category.ifEmpty { "Không có" },
                        fontSize = (10 * scale).sp,
                        fontWeight = FontWeight.Medium,
                        color = categoryTextColor,
                        modifier = Modifier
                            .clip(RoundedCornerShape((4 * scale).dp))
                            .background(categoryBackgroundColor)
                            .padding(horizontal = (6 * scale).dp, vertical = (2 * scale).dp)
                    )
                }

                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Tùy chọn")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape((10 * scale).dp))
                            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape((10 * scale).dp))
                            .width((140 * scale).dp)
                    ) {
                        DropdownMenuItem(onClick = {
                            expanded = false
                            onEdit()
                        }) {
                            Text("Sửa", fontSize = (14 * scale).sp, color = Color.Black)
                        }

                        DropdownMenuItem(onClick = {
                            expanded = false
                            onDelete()
                        }) {
                            Text("Xóa", fontSize = (14 * scale).sp, color = Color.Black)
                        }

                        DropdownMenuItem(onClick = {
                            expanded = false
                            onTogglePin(note.id, !note.isPinned)
                            Toast.makeText(
                                context,
                                if (!note.isPinned) "Đã ghim ghi chú" else "Đã bỏ ghim ghi chú",
                                Toast.LENGTH_SHORT
                            ).show()
                        }) {
                            Text(
                                text = if (note.isPinned) "Bỏ ghim" else "Ghim",
                                fontSize = (14 * scale).sp,
                                color = Color.Black
                            )
                        }

                        DropdownMenuItem(onClick = {
                            expanded = false
                            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "message/rfc822"
                                putExtra(Intent.EXTRA_SUBJECT, "Ghi chú: ${note.title}")
                                putExtra(Intent.EXTRA_TEXT, "${note.description}\n\nDanh mục: ${note.category}")
                            }
                            try {
                                context.startActivity(Intent.createChooser(emailIntent, "Chọn ứng dụng email"))
                            } catch (e: Exception) {
                                Toast.makeText(context, "Không tìm thấy ứng dụng email nào", Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Text("Gửi qua Email", fontSize = (14 * scale).sp, color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}
