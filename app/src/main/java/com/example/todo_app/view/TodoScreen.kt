package com.example.todo_app.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todo_app.model.Todo
import com.example.todo_app.viewmodel.TodoViewModel

@Composable
fun TodoScreen(
    userId: String,
    navController: NavController,
    todoViewModel: TodoViewModel = viewModel(),
) {
    val todos = todoViewModel.todos

    LaunchedEffect(userId) {
        todoViewModel.loadTodos(userId)
    }

    var searchQuery by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf<String?>(null) }
    var selectedDueDate by remember { mutableStateOf<String?>(null) }
    var selectedPriority by remember { mutableStateOf<String?>(null) }

    val filteredTodos = todoViewModel.filterTodos(
        type = selectedType,
        dueDate = selectedDueDate,
        priority = selectedPriority
    ).filter { it.title.contains(searchQuery, ignoreCase = true) }

    val incompleteTodos = filteredTodos.filter { !it.isDone }
    val completedTodos = filteredTodos.filter { it.isDone }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 12.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Danh sách công việc",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6200EE),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                placeholder = { Text("Tìm kiếm theo tên công việc..", fontSize = 12.sp) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Tìm kiếm",
                        tint = Color.Gray
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0xFFF5F5F5),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            // Bộ lọc theo từng dòng
            FilterSection("Loại", listOf("Personal", "Team"), selectedType) { selectedType = it }
            FilterSection("Thời gian", listOf("Today", "Tomorrow"), selectedDueDate) { selectedDueDate = it }
            FilterSection("Ưu tiên", listOf("Low", "Medium", "High"), selectedPriority) { selectedPriority = it }

            Spacer(modifier = Modifier.height(12.dp))

            if (filteredTodos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Không có công việc", fontSize = 16.sp, color = Color.Gray)
                }
            } else {
                LazyColumn {
                    if (incompleteTodos.isNotEmpty()) {
                        item {
                            Text(
                                text = "Chưa hoàn thành",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black,
                                modifier = Modifier.padding(vertical = 6.dp)
                            )
                        }

                        items(incompleteTodos) { todo ->
                            TodoCard(todo) { isChecked ->
                                todoViewModel.updateTodoStatus(todo.id, isChecked)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }

                    if (completedTodos.isNotEmpty()) {
                        item {
                            Text(
                                text = "Đã hoàn thành",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black,
                                modifier = Modifier.padding(vertical = 6.dp)
                            )
                        }

                        items(completedTodos) { todo ->
                            TodoCard(todo) { isChecked ->
                                todoViewModel.updateTodoStatus(todo.id, isChecked)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate("AddTodoScreen/$userId") },
            backgroundColor = Color(0xFF6200EE),
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Thêm")
        }
    }
}

@Composable
fun FilterSection(
    title: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(vertical = 8.dp, horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "$title: ${selectedOption ?: ""}",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f),
                color = Color.Black
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = Color.Gray
            )
        }

        AnimatedVisibility(visible = expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp)
            ) {
                Text(
                    "Tất cả",
                    fontSize = 13.sp,
                    color = if (selectedOption == null) Color(0xFF6200EE) else Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onOptionSelected(null)
                            expanded = false
                        }
                        .padding(vertical = 4.dp)
                )
                options.forEach { option ->
                    Text(
                        option,
                        fontSize = 13.sp,
                        color = if (selectedOption == option) Color(0xFF6200EE) else Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onOptionSelected(option)
                                expanded = false
                            }
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TodoCard(
    todo: Todo,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .clickable { }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color.White)
                .padding(12.dp)
        ) {
            Checkbox(
                checked = todo.isDone,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF6200EE),
                    uncheckedColor = Color.Gray,
                    checkmarkColor = Color.White
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = todo.title,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Color(0xFF512DA8),
                    textDecoration = if (todo.isDone) TextDecoration.LineThrough else TextDecoration.None
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    InfoTag(todo.type, if (todo.type == "Personal") Color(0xFFE3F2FD) else Color(0xFFE8F5E9))
                    InfoTag(todo.dueDate, Color(0xFFFFF3E0))
                    InfoTag(todo.priority, when (todo.priority) {
                        "High" -> Color(0xFFFFEBEE)
                        "Medium" -> Color(0xFFFFF8E1)
                        else -> Color(0xFFE8F5E9)
                    }, when (todo.priority) {
                        "High" -> Color.Red
                        "Medium" -> Color(0xFFFFA000)
                        else -> Color(0xFF388E3C)
                    })
                }
            }
        }
    }
}

@Composable
fun InfoTag(text: String, bgColor: Color, textColor: Color = Color(0xFF424242)) {
    Text(
        text = text,
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium,
        color = textColor,
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(bgColor)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}
