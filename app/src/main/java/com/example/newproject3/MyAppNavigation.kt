package com.example.newproject3

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newproject3.Pages.CarDetailsPage
import com.example.newproject3.Pages.HomePage
import com.example.newproject3.Pages.InAppUsers
import com.example.newproject3.Pages.LoginPage
import com.example.newproject3.Pages.MenuPage
import com.example.newproject3.Pages.NewUserPage
import com.example.newproject3.Pages.RemindersPage
import com.example.newproject3.Pages.SignupPage



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyAppNavigation(modifier: Modifier = Modifier, authViewModel: authViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Login", builder = {
        composable("Login") {
            LoginPage(modifier, navController, authViewModel)
        }
        composable("Signup") {
            SignupPage(modifier, navController, authViewModel)
        }
        composable("home") {
            HomePage(modifier, navController, authViewModel)
        }
        composable("menu") {
            MenuPage(modifier, navController, authViewModel)
        }
        composable("new user") {
            NewUserPage(modifier, navController, authViewModel)
        }
        composable("reminders") {
            RemindersPage(modifier, navController, authViewModel)
        }
        composable("InAppUsers") {
            InAppUsers(modifier, navController, authViewModel)
        }
        composable("carDetails/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            CarDetailsPage(navController = navController, userId = userId)


        }
    })
}

        data class NavItem(
            val label: String,
            val icon: ImageVector,

        )



