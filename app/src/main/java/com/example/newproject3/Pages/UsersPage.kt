package com.example.newproject3.Pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun UsersPage(modifier: Modifier = Modifier, navController: NavController) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BackButton(navController)
        Text(text = "Users Page", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(20.dp))

        // Display user information or a list of users
        Text(text = "User information will be displayed here", fontSize = 18.sp)

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { navController.navigate("home") }) {
            Text(text = "Go to Home")
        }
    }
}
