package com.example.todo_app.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun Register(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)) // Nền đen xám
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(10.dp),
            backgroundColor = Color(0xFF1E1E1E), // Card màu tối
            elevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Tạo Tài Khoản",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFFFFF) // Trắng
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Họ Tên", fontSize = 12.sp, color = Color(0xFFAAAAAA)) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color(0xFFFFFFFF),
                        cursorColor = Color(0xFF6200EE),
                        focusedBorderColor = Color(0xFF6200EE),
                        unfocusedBorderColor = Color(0xFF444444)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email", fontSize = 12.sp, color = Color(0xFFAAAAAA)) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color(0xFFFFFFFF),
                        cursorColor = Color(0xFF6200EE),
                        focusedBorderColor = Color(0xFF6200EE),
                        unfocusedBorderColor = Color(0xFF444444)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Mật khẩu", fontSize = 12.sp, color = Color(0xFFAAAAAA)) },
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color(0xFFFFFFFF),
                        cursorColor = Color(0xFF6200EE),
                        focusedBorderColor = Color(0xFF6200EE),
                        unfocusedBorderColor = Color(0xFF444444)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Xác nhận mật khẩu", fontSize = 12.sp, color = Color(0xFFAAAAAA)) },
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color(0xFFFFFFFF),
                        cursorColor = Color(0xFF6200EE),
                        focusedBorderColor = Color(0xFF6200EE),
                        unfocusedBorderColor = Color(0xFF444444)
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (password != confirmPassword) {
                            Toast.makeText(context, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val user = auth.currentUser
                                    user?.sendEmailVerification()?.addOnCompleteListener { emailTask ->
                                        if (emailTask.isSuccessful) {
                                            val userId = user.uid
                                            val userMap = mapOf(
                                                "name" to name,
                                                "email" to email
                                            )
                                            db.collection("Users").document(userId)
                                                .set(userMap)
                                                .addOnSuccessListener {
                                                    Toast.makeText(
                                                        context,
                                                        "Đăng ký thành công! Vui lòng kiểm tra email để xác nhận.",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                    navController.navigate("login")
                                                }
                                                .addOnFailureListener { e ->
                                                    Toast.makeText(context, "Lưu dữ liệu thất bại: ${e.message}", Toast.LENGTH_LONG).show()
                                                }
                                        } else {
                                            val errorMessage = emailTask.exception?.message ?: "Không thể gửi email xác nhận."
                                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                                        }
                                    }
                                } else {
                                    val errorMessage = task.exception?.message ?: "Đăng ký thất bại."
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                                }
                            }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EE)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Đăng ký tài khoản", color = Color.White, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = {
                    navController.navigate("Login")
                }) {
                    Text("Đã có tài khoản? Đăng nhập", color = Color(0xFF6200EE), fontSize = 10.sp)
                }
            }
        }
    }
}
