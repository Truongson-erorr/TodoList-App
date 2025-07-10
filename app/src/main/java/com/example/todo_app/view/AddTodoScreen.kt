package com.example.todo_app.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todo_app.model.Todo
import com.example.todo_app.viewmodel.TodoViewModel

@Composable
fun AddTodoDialog(
    userId: String,
    onDismiss: () -> Unit,
    onTodoAdded: () -> Unit,
    todoViewModel: TodoViewModel = viewModel()
) {
    var title by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Personal") }
    var dueDate by remember { mutableStateOf("Today") }
    var priority by remember { mutableStateOf("Low") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Thêm việc cần làm",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h6,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                VoiceInputField(
                    value = title,
                    onValueChange = { title = it },
                    label = "Tên công việc",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                FilterDropdown("Loại", listOf("Personal", "Team"), type) {
                    type = it ?: "Personal"
                }

                Spacer(modifier = Modifier.height(12.dp))

                FilterDropdown("Thời gian", listOf("Today", "Tomorrow"), dueDate) {
                    dueDate = it ?: "Today"
                }

                Spacer(modifier = Modifier.height(12.dp))

                FilterDropdown("Ưu tiên", listOf("Low", "Medium", "High"), priority) {
                    priority = it ?: "Low"
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Hủy")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        if (title.isNotBlank()) {
                            val todo = Todo(
                                title = title,
                                userId = userId,
                                isDone = false,
                                type = type,
                                dueDate = dueDate,
                                priority = priority
                            )
                            todoViewModel.addTodo(
                                todo,
                                onSuccess = {
                                    onTodoAdded()
                                },
                                onFailure = { println("Lỗi thêm todo") }
                            )
                        } else {
                            println("Vui lòng nhập tên công việc")
                        }
                    }) {
                        Text("Lưu")
                    }
                }
            }
        }
    }
}

@Composable
fun FilterDropdown(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = { },
            label = { Text(label) },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Chọn $label")
                }
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    onOptionSelected(option)
                    expanded = false
                }) {
                    Text(option)
                }
            }
        }
    }
}