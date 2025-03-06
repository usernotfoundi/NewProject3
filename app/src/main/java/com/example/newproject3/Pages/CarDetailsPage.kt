package com.example.newproject3.Pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// Define a data class for reminders
data class Reminder(val text: String, val daysUntil: String)

@Composable
fun CarDetailsPage(userId: String, navController: NavController) {
    val db: FirebaseFirestore = Firebase.firestore
    var carName by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var reminder by remember { mutableStateOf("") }
    var daysUntil by remember { mutableStateOf("") }
    var reminders by remember { mutableStateOf(listOf<Reminder>()) } // Use the Reminder data class

    // Load car details from Firestore
    LaunchedEffect(userId) {
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    carName = document.getString("carName") ?: "Unknown"
                    brand = document.getString("brand") ?: "Unknown"
                    model = document.getString("model") ?: "Unknown"
                    year = document.getString("year") ?: "Unknown"
                }
            }
        db.collection("users").document(userId).collection("reminders")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    reminders = snapshot.documents.mapNotNull { doc ->
                        val text = doc.getString("Text")
                        val days = doc.getString("daysUntil")
                        if (text != null && days != null) Reminder(text, days) else null
                    }
                }
            }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "Welcome to Your Car details", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Car Name: $carName")
                    Text(text = "Brand: $brand")
                    Text(text = "Model: $model")
                    Text(text = "Year: $year")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                BasicTextField(value = reminder, onValueChange = { reminder = it }, modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                BasicTextField(value = daysUntil, onValueChange = { daysUntil = it }, modifier = Modifier.width(60.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    if (reminder.isNotBlank() && daysUntil.isNotBlank()) {
                        val newReminder = hashMapOf(
                            "Text" to reminder,
                            "daysUntil" to daysUntil
                        )
                        db.collection("users").document(userId).collection("reminders")
                            .add(newReminder)
                        reminder = ""
                        daysUntil = ""
                    }
                }) {
                    Text(text = "+ Add")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Upcoming Reminders", style = MaterialTheme.typography.titleMedium)
            LazyColumn {
                items( }) { reminder ->
                    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        Text(text = "${reminder.text} - ${reminder.daysUntil} days", modifier = Modifier.padding(8.dp))
                    }
                }
            }



            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.popBackStack() }) {
                Text(text = "Back")
            }
        }
    }
}