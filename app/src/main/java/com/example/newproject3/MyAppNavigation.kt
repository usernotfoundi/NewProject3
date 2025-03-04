package com.example.newproject3

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newproject3.Pages.HomePage
import com.example.newproject3.Pages.LoginPage

import com.example.newproject3.Pages.MenuPage
import com.example.newproject3.Pages.NewUserPage
import com.example.newproject3.Pages.RemindersPage
import com.example.newproject3.Pages.SignupPage


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
        composable("menu"){
            MenuPage(modifier ,navController,authViewModel)
        }
        composable("new user"){
            NewUserPage(modifier ,navController,authViewModel)
        }
        composable("reminders"){
            RemindersPage(modifier ,navController,authViewModel)
        }





    } )

    data class NavItem(
        val label : String,
        val icon: ImageVector,

    )
}

