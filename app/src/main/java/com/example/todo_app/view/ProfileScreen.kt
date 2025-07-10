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

    user?.let { currentUser ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF121212))
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Hồ sơ cá nhân",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h5,
                    color = Color(0xFF6200EE)
                )
                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent) // Đảm bảo không bị viền
                ) {
                    Column(
                        modifier = Modifier
                            .background(Color(0xFF2C2C2C)) // Màu nền của card
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
                            color = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currentUser.name,
                            fontSize = 14.sp,
                            style = MaterialTheme.typography.h6,
                            color = Color.White
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
                                    backgroundColor = Color(0xFFE8F5E9),
                                    contentColor = Color(0xFF388E3C)
                                )
                            ) {
                                Text("Gửi email xác thực")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Danh sách ghi chú và công việc cần làm",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
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
                        backgroundColor = Color(0xFF1DE9B6),
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp)
                            .padding(8.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$noteCount",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.h4,
                                    color = Color.White
                                )
                                Text(
                                    fontWeight = FontWeight.Bold,
                                    text = "Ghi chú",
                                    fontSize = 14.sp,
                                    style = MaterialTheme.typography.subtitle1,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Card(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(16.dp),
                        backgroundColor = Color(0xFFFFCC80),
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp)
                            .padding(8.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$todoCount",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.h4,
                                    color = Color.White
                                )
                                Text(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    text = "Công việc",
                                    style = MaterialTheme.typography.subtitle1,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
