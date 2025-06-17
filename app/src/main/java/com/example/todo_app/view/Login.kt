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

@Composable
fun Login(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    // Dark theme colors
    val primaryColor = Color(0xFF6200EE)
    val backgroundColor = Color(0xFF121212)
    val cardColor = Color(0xFF1E1E1E)
    val textColor = Color.White
    val hintColor = Color.Gray

    // Load saved login
    LaunchedEffect(Unit) {
        val sharedPreferences = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        email = sharedPreferences.getString("email", "") ?: ""
        password = sharedPreferences.getString("password", "") ?: ""
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Đăng nhập",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email", fontSize = 12.sp, color = hintColor) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = hintColor,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor,
                        cursorColor = primaryColor
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Mật khẩu", fontSize = 12.sp, color = hintColor) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = hintColor,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor,
                        cursorColor = primaryColor
                    )
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
                                        val sharedPreferences = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
                                        val savedEmail = sharedPreferences.getString("email", null)
                                        if (savedEmail == null) {
                                            showDialog = true
                                        } else {
                                            navController.navigate("MainScreen")
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
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
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
                    Text("Bạn không có tài khoản? Đăng ký", color = primaryColor, fontSize = 10.sp)
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            containerColor = cardColor,
            titleContentColor = textColor,
            textContentColor = hintColor,
            onDismissRequest = { showDialog = false },
            title = { Text("Lưu thông tin đăng nhập") },
            text = { Text("Bạn có muốn lưu thông tin đăng nhập để sử dụng lần sau không?") },
            confirmButton = {
                Button(
                    onClick = {
                        val sharedPreferences = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
                        sharedPreferences.edit()
                            .putString("email", email)
                            .putString("password", password)
                            .apply()
                        Toast.makeText(context, "Thông tin đăng nhập đã được lưu.", Toast.LENGTH_SHORT).show()
                        showDialog = false
                        navController.navigate("MainScreen")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                ) {
                    Text("Lưu", color = Color.Black)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog = false
                        navController.navigate("MainScreen")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Không", color = Color.White)
                }
            }
        )
    }
}
