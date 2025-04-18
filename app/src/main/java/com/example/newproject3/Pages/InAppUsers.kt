package com.example.newproject3.Pages

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newproject3.authViewModel
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun InAppUsers(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: authViewModel
) {
    var users by remember { mutableStateOf(listOf<String>()) }
    val db = FirebaseFirestore.getInstance()

    // New/Edit user input states
    var showForm by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var carName by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var engine by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }

    var isEditing by remember { mutableStateOf(false) }
    var editingUserId by remember { mutableStateOf<String?>(null) }

    fun loadUsers() {
        db.collection("userInfo").get()
            .addOnSuccessListener { result ->
                users = result.documents.mapNotNull { it.id }
            }
    }

    LaunchedEffect(Unit) {
        loadUsers()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Select a User", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(users) { userId ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            userId,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.clickable {
                                navController.navigate("menu/$userId")
                            })

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(onClick = {
                                db.collection("userInfo").document(userId).get()
                                    .addOnSuccessListener { document ->
                                        name = document.getString("name") ?: ""
                                        carName = document.getString("carName") ?: ""
                                        brand = document.getString("carBrand") ?: ""
                                        engine = document.getString("engine") ?: ""
                                        year = document.getString("year") ?: ""
                                        editingUserId = userId
                                        isEditing = true
                                        showForm = true
                                    }
                            }) {
                                Text("Edit")
                            }

                            TextButton(onClick = {
                                db.collection("userInfo").document(userId).delete()
                                    .addOnSuccessListener {
                                        loadUsers()
                                    }
                            }) {
                                Text("Delete", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                showForm = !showForm
                if (!showForm) {
                    isEditing = false
                    editingUserId = null
                    name = ""
                    carName = ""
                    brand = ""
                    engine = ""
                    year = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (showForm) "Cancel" else "Add A User")
        }

        if (showForm) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                if (isEditing) "Edit User" else "Add New User",
                style = MaterialTheme.typography.titleMedium
            )

            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth().padding(4.dp)
            )
            TextField(
                value = carName,
                onValueChange = { carName = it },
                label = { Text("Car Name") },
                modifier = Modifier.fillMaxWidth().padding(4.dp)
            )
            TextField(
                value = brand,
                onValueChange = { brand = it },
                label = { Text("Car Brand") },
                modifier = Modifier.fillMaxWidth().padding(4.dp)
            )
            TextField(
                value = engine,
                onValueChange = { engine = it },
                label = { Text("Engine") },
                modifier = Modifier.fillMaxWidth().padding(4.dp)
            )
            TextField(
                value = year,
                onValueChange = { year = it },
                label = { Text("Year") },
                modifier = Modifier.fillMaxWidth().padding(4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val db = FirebaseFirestore.getInstance()

                        val userMap = hashMapOf(
                            "name" to name,
                            "carName" to carName,
                            "carBrand" to brand,
                            "engine" to engine,
                            "year" to year
                        )

                        val userId = name.trim() // Use name as doc ID

                        db.collection("userInfo").document(userId)
                            .set(userMap)
                            .addOnSuccessListener {
                                Log.d("Firestore", "User successfully added with ID: $userId")

                                // Reset form
                                name = ""
                                carName = ""
                                brand = ""
                                engine = ""
                                year = ""
                                showForm = false
                                isEditing = false
                                editingUserId = null

                                // Navigate to user's Car Details screen
                                navController.navigate("menu/$userId")
                            }
                            .addOnFailureListener { e ->
                                Log.e("Firestore", "Error writing document", e)
                            }
                    } else {
                        Log.w("SubmitButton", "Name field is blank. User not created.")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditing) "Save Changes" else "Submit")
            }

        }
    }
}