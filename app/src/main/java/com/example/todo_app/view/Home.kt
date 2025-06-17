package com.example.todo_app.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    Scaffold(
        backgroundColor = Color.Black,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.Black)
            ) {
                BottomNavigation(
                    backgroundColor = Color.Transparent,
                    contentColor = Color.White,
                    elevation = 0.dp,
                    modifier = Modifier
                        .height(34.dp)
                ) {
                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.Edit, contentDescription = "To Do", modifier = Modifier.size(18.dp) ) },
                        label = { Text("Note", fontSize = 10.sp) },
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        selectedContentColor = Color(0xFF6200EE),
                        unselectedContentColor = Color.White
                    )

                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.List, contentDescription = "To Do", modifier = Modifier.size(18.dp)) },
                        label = { Text("To Do", fontSize = 10.sp) },
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        selectedContentColor = Color(0xFF6200EE),
                        unselectedContentColor = Color.White
                    )
                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.DateRange, contentDescription = "To Do", modifier = Modifier.size(18.dp)) },
                        label = { Text("Calendar", fontSize = 10.sp) },
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        selectedContentColor = Color(0xFF6200EE),
                        unselectedContentColor = Color.White
                    )
                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.AccountCircle, contentDescription = "To Do", modifier = Modifier.size(18.dp)) },
                        label = { Text("Profile", fontSize = 10.sp) },
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        selectedContentColor = Color(0xFF6200EE),
                        unselectedContentColor = Color.White
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFFF2F2F2)) // Màu nền nhẹ nhàng
        ) {
            when (selectedTab) {
                0 -> NoteScreen(userId, navController)
                1 -> TodoScreen(userId, navController)
                2 -> CalendarScreen(userId, navController)
                3 -> ProfileScreen(userId, navController)
            }
        }
    }
}





