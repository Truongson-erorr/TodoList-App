package com.example.todo_app.view

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import androidx.compose.material3.AlertDialog

@Composable
fun Login(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) } // State để hiển thị dialog

    val primaryColor = Color(0xFF1976D2) // Blue
    val backgroundColor = Color(0xFFF1F6FA)

    // Load thông tin đăng nhập đã lưu nếu có
    LaunchedEffect(Unit) {
        val sharedPreferences = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        email = sharedPreferences.getString("email", "") ?: ""
        password = sharedPreferences.getString("password", "") ?: ""
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F6FA))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Đăng nhập",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email", fontSize = 12.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Mật khẩu", fontSize = 12.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val user = auth.currentUser
                                    if (user != null && user.isEmailVerified) {
                                        Toast.makeText(context, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                                        // Kiểm tra xem thông tin đã được lưu chưa
                                        val sharedPreferences = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
                                        val savedEmail = sharedPreferences.getString("email", null)
                                        if (savedEmail == null) {
                                            showDialog = true // Chỉ hiển thị dialog nếu chưa lưu
                                        } else {
                                            navController.navigate("MainScreen") // Chuyển hướng ngay nếu đã lưu
                                        }
                                    } else {
                                        Toast.makeText(context, "Vui lòng xác minh email trước khi đăng nhập.", Toast.LENGTH_LONG).show()
                                        auth.signOut()
                                    }
                                } else {
                                    val errorMessage = task.exception?.message ?: "Đăng nhập thất bại."
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                                }
                            }
                    },
                    colors = ButtonDefaults.buttonColors(Color.Black),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    Text("Đăng nhập", color = Color.White, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = {
                    navController.navigate("register")
                }) {
                    Text("Bạn không có tài khoản? Đăng ký", color = Color.Black, fontSize = 10.sp)
                }
            }
        }
    }

    // Dialog hỏi lưu thông tin đăng nhập
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Lưu thông tin đăng nhập") },
            text = { Text("Bạn có muốn lưu thông tin đăng nhập để sử dụng lần sau không?") },
            confirmButton = {
                Button(
                    onClick = {
                        // Lưu thông tin vào SharedPreferences
                        val sharedPreferences = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
                        sharedPreferences.edit()
                            .putString("email", email)
                            .putString("password", password)
                            .apply()
                        Toast.makeText(context, "Thông tin đăng nhập đã được lưu.", Toast.LENGTH_SHORT).show()
                        showDialog = false
                        navController.navigate("MainScreen")
                    }
                ) {
                    Text("Lưu")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        // Không lưu, chỉ chuyển hướng
                        showDialog = false
                        navController.navigate("MainScreen")
                    }
                ) {
                    Text("Không")
                }
            }
        )
    }
}