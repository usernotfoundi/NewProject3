package com.example.newproject3.Pages


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.newproject3.authViewModel



@Composable
fun MenuPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: authViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE6CCFF)) // Light Purple Background
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Menu",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(20.dp))

        MenuItemCard(text = "New User", onClick = { navController.navigate("new user") })
        MenuItemCard(text = "Reminders", onClick = {navController.navigate("Reminders")})
        MenuItemCard(text = "Maintenance Tracking", onClick = { /* Handle Click */ })
        MenuItemCard(text = "Gas Stations Near Me", onClick = { /* Handle Click */ })
        MenuItemCard(text = "Users", onClick = { navController.navigate("users")})
        MenuItemCard(text = "MainScreen", onClick = {navController.navigate("MainScreen")})

        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { navController.navigate("home") }) {
            Text(text = "Go to Home")
        }
    }
}

@Composable
fun MenuItemCard(text: String, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text, fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color.Black)
        }
    }
}
