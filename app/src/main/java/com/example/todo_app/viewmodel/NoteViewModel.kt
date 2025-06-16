package com.example.todo_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_app.model.Note
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NoteViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    fun loadNotes(userId: String) {
        db.collection("Notes")
            .whereEqualTo("userId", userId)
            // Lắng nghe thay đổi trong thời gian thực
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Xử lý lỗi
                    return@addSnapshotListener
                }

                snapshot?.let { result ->
                    val noteList = result.map { doc ->
                        Note(
                            id = doc.id,
                            userId = doc.getString("userId") ?: "",
                            title = doc.getString("title") ?: "",
                            description = doc.getString("description") ?: "",
                            isPinned = doc.getBoolean("isPinned") ?: false,
                            date = doc.getString("date") ?: "",  // Lấy thêm date
                            category = doc.getString("category") ?: ""
                        )
                    }
                    _notes.value = noteList
                }
            }
    }

    fun addNote(note: Note, onSuccess: () -> Unit) {
        viewModelScope.launch {
            db.collection("Notes")
                .add(note)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { /* có thể xử lý lỗi ở đây nếu muốn */ }
        }
    }

    fun getNoteById(noteId: String): Note? {
        return notes.value.find { it.id == noteId }
    }

    fun updateNote(noteId: String, category: String, title: String, description: String, date: String? = null, onSuccess: () -> Unit) {
        val updates = mutableMapOf<String, Any>(
            "title" to title,
            "description" to description,
            "category" to category
        )
        date?.let {
            updates["date"] = it
        }
        db.collection("Notes").document(noteId)
            .update(updates)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { /* handle error */ }
    }

    fun deleteNote(noteId: String) {
        db.collection("Notes").document(noteId)
            .delete()
            .addOnSuccessListener {  }
            .addOnFailureListener {  }
    }

    fun togglePinNote(noteId: String, isPinned: Boolean) {
        db.collection("Notes").document(noteId)
            .update("isPinned", isPinned)
            .addOnFailureListener {  }
    }
}