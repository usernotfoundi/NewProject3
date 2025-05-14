package com.example.newproject3.Pages

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.example.newproject3.User // ✅ Use your own User class, not Firebase's

data class Reminder(val text: String, val daysUntil: String)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CarDetailsPage(userId: String, navController: NavController) {
    val db = FirebaseFirestore.getInstance()

    var carName by remember { mutableStateOf("") }
    var Brand by remember { mutableStateOf("") }
    var engine by remember { mutableStateOf("") }
    var Year by remember { mutableStateOf("") }
    var reminder by remember { mutableStateOf("") }
    var daysUntil by remember { mutableStateOf("") }
    var reminders by remember { mutableStateOf(listOf<Reminder>()) }

    // Load car details from "User" collection
    LaunchedEffect(userId) {
        db.collection("User").document(userId)
            .addSnapshotListener { document, e ->
                if (e != null) {
                    Log.e("Firestore", "Error fetching car details", e)
                    return@addSnapshotListener
                }
                document?.let { doc ->
                    val user = doc.toObject(User::class.java) // ✅ Using your app's User data class
                    user?.let {
                        Brand = it.Brand
                        carName = it.CarName
                        engine = it.engine
                        Year = it.Year.toString()
                        Log.d("CarDetails", "Fetched from Firestore -> brand=$Brand, carName=$carName, engine=$engine, Year=$Year")
                    }
                }
            }
    }

    // Load reminders from nested "Task" collection
    LaunchedEffect(userId) {
        db.collection("User").document(userId).collection("Task")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.e("Firestore", "Error fetching reminders", e)
                    return@addSnapshotListener
                }
                val updatedReminders = mutableListOf<Reminder>()
                value?.documents?.forEach { doc ->
                    val text = doc.getString("Text") ?: ""
                    val days = doc.getLong("daysUntil")?.toString() ?: "0"
                    updatedReminders.add(Reminder(text, days))
                }
                reminders = updatedReminders
            }
    }

    // UI
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Car Details", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Card(Modifier.fillMaxWidth().padding(8.dp)) {
            Column(Modifier.padding(16.dp)) {
                Text("Brand: $Brand")
                Text("Car Name: $carName")
                Text("Engine: $engine")
                Text("Year: $Year")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            BasicTextField(value = reminder, onValueChange = { reminder = it }, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            BasicTextField(value = daysUntil, onValueChange = { daysUntil = it }, modifier = Modifier.width(60.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (reminder.isNotBlank() && daysUntil.isNotBlank()) {
                    val newReminder = hashMapOf("Text" to reminder, "daysUntil" to (daysUntil.toIntOrNull() ?: 0))
                    db.collection("User").document(userId).collection("Task").add(newReminder)
                        .addOnSuccessListener {
                            reminder = ""
                            daysUntil = ""
                        }.addOnFailureListener { e ->
                            Log.e("Firestore", "Error adding reminder", e)
                        }
                }
            }) {
                Text("+ Add")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Reminders", style = MaterialTheme.typography.titleMedium)
        LazyColumn {
            items(reminders, key = { it.hashCode() }) {
                Card(Modifier.fillMaxWidth().padding(8.dp)) {
                    Column(Modifier.padding(8.dp)) {
                        Text(it.text)
                        Text("Due in: ${it.daysUntil} days")
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
