package com.example.todo_app.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import com.example.todo_app.view.IntroNoteScreen
import com.example.todo_app.view.IntroTodoScreen
import com.example.todo_app.view.Login
import com.example.todo_app.view.MainScreen
import com.example.todo_app.view.NoteByDateScreen
import com.example.todo_app.view.NoteScreen
import com.example.todo_app.view.ProfileScreen
import com.example.todo_app.view.Register
import com.example.todo_app.view.TodoScreen
import com.example.todo_app.view.WelcomeScreen
import com.google.firebase.auth.FirebaseAuth

object Routes {
    const val NoteScreen = "NoteScreen/{userId}"
    const val AddNoteScreen = "AddNoteScreen/{userId}"
    const val EditNoteScreen = "EditNoteScreen/{noteId}/{userId}"
    const val CalendarScreen = "CalendarScreen"
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid ?: ""

    AnimatedNavHost(
        navController = navController,
        startDestination = "WelcomeScreen",
        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(300)) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(300)) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(300)) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(300)) }
    ) {
        composable("Register") { Register(navController) }
        composable("Login") { Login(navController) }
        composable("MainScreen") { MainScreen(navController) }
        composable("IntroNote") { IntroNoteScreen(navController) }
        composable("IntroTodo") { IntroTodoScreen(navController) }
        composable("WelcomeScreen") { WelcomeScreen(navController) }
        composable("NoteScreen/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            NoteScreen(userId, navController)
        }

        composable("TodoScreen/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            TodoScreen(userId, navController)
        }

        composable(
            route = "profile/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            ProfileScreen(userId = userId, navController = navController)
        }

        composable(
            route = "NoteByDateScreen/{userId}/{date}",
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val date = backStackEntry.arguments?.getString("date") ?: ""
            NoteByDateScreen(userId = userId, selectedDate = date, navController = navController)
        }
    }
}
