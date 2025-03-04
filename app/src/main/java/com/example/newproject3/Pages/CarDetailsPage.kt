package com.example.newproject3.Pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun CarDetailsPage(userId: String) {
    val db: FirebaseFirestore = Firebase.firestore
    var carName by remember { mutableStateOf("InAppUsers") }
    var brand by remember { mutableStateOf("InAppUsers") }
    var model by remember { mutableStateOf("InAppUsers") }
    var year by remember { mutableStateOf("InAppUsers") }
    var reminder by remember { mutableStateOf("InAppUsers") }
    var daysUntil by remember { mutableStateOf("InAppUsers") }
    var reminders by remember { mutableStateOf(listOf<Pair<String, String>>()) } // Define reminders list

    // Load car details from Firestore
    LaunchedEffect(userId) {
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    carName = document.getString("carName") ?: ""
                    brand = document.getString("brand") ?: ""
                    model = document.getString("model") ?: ""
                    year = document.getString("year") ?: ""
                }
            }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Car Details", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Name: $carName")
        Text(text = "Brand: $brand")
        Text(text = "Model: $model")
        Text(text = "Year: $year")

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
                        .addOnSuccessListener {
                            reminders = reminders + (reminder to daysUntil)
                            reminder = ""
                            daysUntil = ""
                        }
                }
            }) {
                Text(text = "+ Add")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        reminders.forEach { (text, days) ->
            Text(text = "$text - $days days")
        }
    }
}
