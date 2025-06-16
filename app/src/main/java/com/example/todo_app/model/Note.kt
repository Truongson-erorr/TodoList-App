package com.example.todo_app.model

data class Note(
    val id: String="",
    val title: String="",
    val description: String="",
    val userId: String="",
    val date: String,
    val isPinned: Boolean = false,
    val category: String = ""
)
