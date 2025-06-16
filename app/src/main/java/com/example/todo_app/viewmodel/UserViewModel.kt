package com.example.todo_app.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.todo_app.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    var user = mutableStateOf<Users?>(null)
        private set
    var loading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)
    var successMessage = mutableStateOf<String?>(null)
    var isEmailVerified = mutableStateOf(false)
    var noteCount = mutableStateOf(0)
    var todoCount = mutableStateOf(0)

    fun loadUser(userId: String) {
        loading.value = true
        // Kiểm tra xác thực email
        isEmailVerified.value = auth.currentUser?.isEmailVerified ?: false

        // Lấy thông tin người dùng
        db.collection("Users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    user.value = document.toObject(Users::class.java)
                } else {
                    errorMessage.value = "Người dùng không tồn tại"
                }
                // Lấy số lượng note
                db.collection("Notes").whereEqualTo("userId", userId).get()
                    .addOnSuccessListener { notes ->
                        noteCount.value = notes.size()
                        // Lấy số lượng todo
                        db.collection("Todos").whereEqualTo("userId", userId).get()
                            .addOnSuccessListener { todos ->
                                todoCount.value = todos.size()
                                loading.value = false
                            }
                            .addOnFailureListener { e ->
                                loading.value = false
                                errorMessage.value = e.message
                            }
                    }
                    .addOnFailureListener { e ->
                        loading.value = false
                        errorMessage.value = e.message
                    }
            }
            .addOnFailureListener { e ->
                loading.value = false
                errorMessage.value = e.message
            }
    }

    fun sendEmailVerification(onComplete: (Boolean, String?) -> Unit) {
        val firebaseUser = auth.currentUser
        if (firebaseUser == null) {
            onComplete(false, "Chưa đăng nhập")
            return
        }
        if (firebaseUser.isEmailVerified) {
            onComplete(false, "Email đã được xác thực")
            return
        }
        firebaseUser.sendEmailVerification()
            .addOnSuccessListener {
                onComplete(true, "Đã gửi email xác thực")
            }
            .addOnFailureListener { e ->
                onComplete(false, e.message)
            }
    }

    fun updateName(newName: String) {
        val currentUser = user.value ?: return
        loading.value = true
        db.collection("Users").document(currentUser.id)
            .update("name", newName)
            .addOnSuccessListener {
                loading.value = false
                user.value = currentUser.copy(name = newName)
                successMessage.value = "Đã cập nhật tên"
            }
            .addOnFailureListener { e ->
                loading.value = false
                errorMessage.value = e.message
            }
    }

    fun changePassword(oldPassword: String, newPassword: String, onComplete: (Boolean, String?) -> Unit) {
        val firebaseUser = auth.currentUser
        val email = firebaseUser?.email
        if (firebaseUser == null || email == null) {
            onComplete(false, "Chưa đăng nhập")
            return
        }

        val credential = com.google.firebase.auth.EmailAuthProvider.getCredential(email, oldPassword)
        firebaseUser.reauthenticate(credential)
            .addOnSuccessListener {
                firebaseUser.updatePassword(newPassword)
                    .addOnSuccessListener {
                        onComplete(true, "Đổi mật khẩu thành công")
                    }
                    .addOnFailureListener { e ->
                        onComplete(false, e.message)
                    }
            }
            .addOnFailureListener { e ->
                onComplete(false, "Mật khẩu cũ không đúng hoặc lỗi: ${e.message}")
            }
    }

    fun logout(onComplete: () -> Unit = {}) {
        auth.signOut()
        user.value = null
        isEmailVerified.value = false
        noteCount.value = 0
        todoCount.value = 0
        onComplete()
    }
}