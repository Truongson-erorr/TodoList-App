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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todo_app.model.Todo
import com.example.todo_app.viewmodel.TodoViewModel

@Composable
fun AddTodoScreen(
    userId: String,
    navController: NavController,
    todoViewModel: TodoViewModel = viewModel(),
) {
    var title by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Personal") }
    var dueDate by remember { mutableStateOf("Today") }
    var priority by remember { mutableStateOf("Low") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "Thêm việc cần làm",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.h5,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(24.dp))

        VoiceInputField(
            value = title,
            onValueChange = { title = it },
            label = "Tên công việc",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        FilterDropdown(
            label = "Loại",
            options = listOf("Personal", "Team"),
            selectedOption = type,
            onOptionSelected = { type = it ?: "Personal" }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Dropdown cho Thời gian
        FilterDropdown(
            label = "Thời gian",
            options = listOf("Today", "Tomorrow"),
            selectedOption = dueDate,
            onOptionSelected = { dueDate = it ?: "Today" }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Dropdown cho Ưu tiên
        FilterDropdown(
            label = "Ưu tiên",
            options = listOf("Low", "Medium", "High"),
            selectedOption = priority,
            onOptionSelected = { priority = it ?: "Low" }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
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
                        onSuccess = { navController.popBackStack() },
                        onFailure = { exception -> println("Lỗi thêm todo: $exception") }
                    )
                } else {
                    // Hiển thị thông báo nếu tiêu đề trống
                    // Bạn có thể thay bằng Toast nếu cần
                    println("Vui lòng nhập tên công việc")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text("Lưu", style = MaterialTheme.typography.button)
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