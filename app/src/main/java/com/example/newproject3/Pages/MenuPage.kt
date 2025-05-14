package com.example.newproject3.Pages


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.newproject3.authViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun MenuPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: authViewModel,
    userId: String
) {
    var currentUserName by remember { mutableStateOf("") }

    // Load user's name from Firestore
    LaunchedEffect(userId) {
        val db = Firebase.firestore
        db.collection("User").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    currentUserName = document.getString("name") ?: "Unknown User"
                }
            }
            .addOnFailureListener {
                currentUserName = "Failed to load user"
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE6CCFF))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Current User: $currentUserName",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )

        Text(
            text = "Menu",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(20.dp))

        MenuItemCard(text = "Reminders") {
            navController.navigate("reminders")
        }
        MenuItemCard(text = "InAppUsers") {
            navController.navigate("InAppUsers")
        }
        MenuItemCard(text = "Gas Stations Near Me") {
            navController.navigate("Gas stations near me")
        }
        MenuItemCard(text = "Users") {
            navController.navigate("users")
        }
        MenuItemCard(text = "Car Details") {
            navController.navigate("carDetailsPage/$userId")
        }

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
            Text(
                text = text,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}
