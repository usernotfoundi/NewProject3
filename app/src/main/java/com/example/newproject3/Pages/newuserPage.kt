package com.example.newproject3.Pages

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newproject3.authViewModel
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun NewUserPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: authViewModel
) {
    var name by remember { mutableStateOf("") }
    var carName by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }

    val db = FirebaseFirestore.getInstance()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        BackButton(navController)

        Text("New User", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        TextField(
            value = carName,
            onValueChange = { carName = it },
            label = { Text("Car Name") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        TextField(
            value = brand,
            onValueChange = { brand = it },
            label = { Text("Car Brand") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        TextField(
            value = model,
            onValueChange = { model = it },
            label = { Text("Model") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        TextField(
            value = year,
            onValueChange = { year = it },
            label = { Text("Year") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (name.isNotBlank() && carName.isNotBlank() && brand.isNotBlank() && model.isNotBlank() && year.isNotBlank()) {
                    val user = hashMapOf(
                        "name" to name,
                        "carName" to carName,
                        "carBrand" to brand,
                        "model" to model,
                        "year" to year,
                    )

                    db.collection("userInfo").document(name).set(user)
                        .addOnSuccessListener {
                            navController.navigate("users")
                        }
                        .addOnFailureListener {
                            // Optional: Show error message or log
                        }
                } else {
                    // Optional: Show a Toast or Snackbar that fields must be filled
                }
            }
        ) {
            Text("Submit")
        }
    }
}

@Composable
fun BackButton(navController: NavController) {
    IconButton(onClick = { navController.navigateUp() }) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
    }
}
