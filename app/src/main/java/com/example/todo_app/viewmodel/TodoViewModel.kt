package com.example.todo_app.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.todo_app.model.Todo
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class TodoViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    // Danh sách công việc
    val todos = mutableStateListOf<Todo>()

    fun loadTodos(userId: String) {
        db.collection("Todos")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val oldStatusMap = todos.associate { it.id to it.isDone }
                todos.clear()
                for (document in result) {
                    val todoFromFirestore = document.toObject(Todo::class.java).copy(id = document.id)
                    val isDone = oldStatusMap[todoFromFirestore.id] ?: todoFromFirestore.isDone
                    val todoWithCorrectStatus = todoFromFirestore.copy(isDone = isDone)
                    todos.add(todoWithCorrectStatus)
                }
            }
            .addOnFailureListener {
                // xử lý lỗi
            }
    }

    fun addTodo(todo: Todo, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val newTodo = todo.copy(id = UUID.randomUUID().toString())
        db.collection("Todos")
            .document(newTodo.id)
            .set(newTodo)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun updateTodoStatus(todoId: String, isDone: Boolean) {
        db.collection("Todos").document(todoId)
            .update("isDone", isDone)
            .addOnSuccessListener {
                val index = todos.indexOfFirst { it.id == todoId }
                if (index != -1) {
                    val updatedTodo = todos[index].copy(isDone = isDone)
                    todos[index] = updatedTodo
                }
            }
            .addOnFailureListener {
                // xử lý lỗi
            }
    }

    // Lọc công việc theo type, dueDate, priority
    fun filterTodos(
        type: String? = null,
        dueDate: String? = null,
        priority: String? = null
    ): List<Todo> {
        return todos.filter { todo ->
            (type == null || todo.type == type) &&
                    (dueDate == null || todo.dueDate == dueDate) &&
                    (priority == null || todo.priority == priority)
        }
    }
}