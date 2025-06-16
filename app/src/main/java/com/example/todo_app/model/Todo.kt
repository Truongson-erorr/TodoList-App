package com.example.todo_app.model

data class Todo(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val isDone: Boolean = false,
    val type: String = "Personal", // Cá nhân hoặc Team
    val dueDate: String = "Today", // Hôm nay hoặc Ngày mai
    val priority: String = "Low"    // Thấp, Trung bình, Cao
)