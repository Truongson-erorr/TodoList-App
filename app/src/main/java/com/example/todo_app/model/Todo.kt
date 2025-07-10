package com.example.todo_app.model

data class Todo(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val isDone: Boolean = false,
    val type: String = "Personal",
    val dueDate: String = "Today",
    val priority: String = "Low"
)