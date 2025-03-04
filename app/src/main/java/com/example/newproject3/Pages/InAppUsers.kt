package com.example.newproject3.Pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.newproject3.authViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

@Composable
fun InAppUsers(modifier: Modifier, navController: NavHostController, authViewModel: authViewModel) {
    val db: FirebaseFirestore = Firebase.firestore
    var users by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }

    LaunchedEffect(Unit) {
        db.collection("users").get().addOnSuccessListener { result ->
            users = result.documents.mapNotNull { doc ->
                val userName = doc.getString("name") ?: "Unknown"
                val userId = doc.id
                userName to userId
            }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Select a User", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        users.forEach { (name, userId) ->
            if (userId.isNotEmpty()) {
                Text(
                    text = name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            navController.navigate("carDetails/$userId")
                        }
                )
            } else {
                Text(text = "Invalid User Id", modifier = modifier.padding(8.dp))
            }
        }
    }
}