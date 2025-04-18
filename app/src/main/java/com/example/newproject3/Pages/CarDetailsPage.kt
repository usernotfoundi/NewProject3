package com.example.newproject3.Pages

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore



// Define a data class for reminders
data class Reminder(val text: String, val daysUntil: String)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CarDetailsPage(userId: String, navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    var carName by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var reminder by remember { mutableStateOf("") }
    var daysUntil by remember { mutableStateOf("") }
    var reminders by remember { mutableStateOf(listOf<Reminder>()) }

    LaunchedEffect(userId) {
        db.collection("User").document(userId)
            .addSnapshotListener { document, e ->
                if (e != null) {
                    Log.e("Firestore", "Error fetching car details", e)
                    return@addSnapshotListener
                }
                if (document != null && document.exists()) {
                    carName = document.getString("CarName") ?: "Unknown"
                    brand = document.getString("Brand") ?: "Unknown"
                    model = document.getString("Model") ?: "Unknown"
                    year = document.getString("Year") ?: "Unknown"
                }
            }
    }

    LaunchedEffect(userId) {
        db.collection("User").document(userId).collection("Task")
            .addSnapshotListener { result, e ->
                if (e != null) {
                    Log.e("Firestore", "Error fetching reminders", e)
                    return@addSnapshotListener
                }
                if (result != null) {
                    reminders = result.documents.mapNotNull {
                        val text = it.getString("Text") ?: return@mapNotNull null
                        val days = it.getLong("daysUntil")?.toString() ?: "0"
                        Reminder(text, days)
                    }
                }
            }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Welcome to Your Car Details", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth().padding(8.dp), elevation = CardDefaults.cardElevation(4.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Car Name: $carName")
                Text("Brand: $brand")
                Text("Model: $model")
                Text("Year: $year")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth().padding(8.dp), elevation = CardDefaults.cardElevation(4.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    BasicTextField(value = reminder, onValueChange = { reminder = it }, modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    BasicTextField(value = daysUntil, onValueChange = { daysUntil = it }, modifier = Modifier.width(60.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        if (reminder.isNotBlank() && daysUntil.isNotBlank()) {
                            val daysUntilInt = daysUntil.toIntOrNull() ?: 0 // Ensure valid integer
                            val newReminder = hashMapOf("Text" to reminder, "daysUntil" to daysUntilInt)
                            db.collection("User").document(userId).collection("Task").add(newReminder)
                                .addOnSuccessListener {
                                    reminder = ""
                                    daysUntil = ""
                                }
                                .addOnFailureListener { e ->
                                    Log.e("Firestore", "Error adding reminder", e)
                                }
                        }
                    }) {
                        Text("+ Add")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Upcoming Reminders", style = MaterialTheme.typography.titleMedium)
        LazyColumn {
            items(reminders, key = { it.hashCode() }) { reminder ->
                Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(reminder.text)
                        Text("Due in: ${reminder.daysUntil} days")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Back")
        }
    }
}
