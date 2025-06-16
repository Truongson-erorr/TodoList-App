package com.example.todo_app.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todo_app.viewmodel.UserViewModel

@Composable
fun ProfileScreen(
    userId: String,
    navController: NavController,
    userViewModel: UserViewModel = viewModel()
) {
    val user by userViewModel.user
    val loading by userViewModel.loading
    val isEmailVerified by userViewModel.isEmailVerified
    val noteCount by userViewModel.noteCount
    val todoCount by userViewModel.todoCount
    val successMessage by userViewModel.successMessage
    val errorMessage by userViewModel.errorMessage

    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }
    var showChangePasswordDialog by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        userViewModel.loadUser(userId)
    }

    LaunchedEffect(successMessage, errorMessage) {
        successMessage?.let { println(it) }
        errorMessage?.let { println(it) }
    }

    if (loading) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    user?.let { currentUser ->
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Hồ sơ cá nhân",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h5,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(24.dp))

            Card(
                elevation = 8.dp,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Avatar",
                        modifier = Modifier.size(70.dp),
                        tint = MaterialTheme.colors.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = currentUser.email,
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = currentUser.name,
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.h6
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (isEmailVerified) Color(0xFFE8F5E9) else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (isEmailVerified) "Bạn đã được xác thực Email!" else "Email chưa được xác thực",
                            style = MaterialTheme.typography.body2,
                            color = if (isEmailVerified) Color(0xFF388E3C) else Color.Red
                        )
                    }
                    if (!isEmailVerified) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                userViewModel.sendEmailVerification { success, message ->
                                    println(message)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFE8F5E9), // Màu xanh lá nhạt
                                contentColor = Color(0xFF388E3C) // Đổi màu chữ thành đen để dễ đọc trên nền xanh nhạt
                            )
                        ) {
                            Text("Gửi email xác thực")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Danh sách công việc",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Card(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = Color(0xFFE3F2FD),
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .padding(8.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$noteCount",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.h4,
                                color = Color.Black
                            )
                            Text(
                                fontWeight = FontWeight.Bold,
                                text = "Ghi chú",
                                fontSize = 14.sp,
                                style = MaterialTheme.typography.subtitle1,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
                Card(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = Color(0xFFFFEBEE),
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .padding(8.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$todoCount",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.h4,
                                color = Color.Black
                            )
                            Text(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                text = "Công việc",
                                style = MaterialTheme.typography.subtitle1,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { showChangePasswordDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text("Đổi mật khẩu", fontSize = 10.sp,style = MaterialTheme.typography.button)
            }
        }
    }

    if (showChangePasswordDialog) {
        AlertDialog(
            onDismissRequest = { showChangePasswordDialog = false },
            title = { Text("Đổi mật khẩu") },
            text = {
                Column {
                    OutlinedTextField(
                        value = oldPassword,
                        onValueChange = { oldPassword = it },
                        label = { Text("Mật khẩu cũ") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("Mật khẩu mới") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = confirmNewPassword,
                        onValueChange = { confirmNewPassword = it },
                        label = { Text("Xác nhận mật khẩu mới") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newPassword != confirmNewPassword) {
                        println("Mật khẩu mới không khớp")
                        return@TextButton
                    }
                    if (newPassword.length < 6) {
                        println("Mật khẩu mới phải ít nhất 6 ký tự")
                        return@TextButton
                    }
                    userViewModel.changePassword(oldPassword, newPassword) { success, message ->
                        if (success) {
                            println("Đổi mật khẩu thành công")
                            showChangePasswordDialog = false
                            oldPassword = ""
                            newPassword = ""
                            confirmNewPassword = ""
                        } else {
                            println("Lỗi đổi mật khẩu: $message")
                        }
                    }
                }) {
                    Text("Xác nhận")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showChangePasswordDialog = false
                    oldPassword = ""
                    newPassword = ""
                    confirmNewPassword = ""
                }) {
                    Text("Hủy")
                }
            }
        )
    }
}