package com.example.newproject3.Pages

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.newproject3.AuthState
import com.example.newproject3.authViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore


@Composable
fun RemindersPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: authViewModel) {

    var reminder by remember {
        mutableStateOf("")
    }
    var reminder2 by remember {
        mutableStateOf("")
    }
    var reminder3 by remember {
        mutableStateOf("")
    }
    var reminder4 by remember {
        mutableStateOf("")
    }


    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current
    val db: FirebaseFirestore = Firebase.firestore

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> navController.navigate("Reminders")
            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT
            ).show()

            else -> Unit
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Reminders", fontSize = 32.sp)

        Spacer(modifier = modifier.height(16.dp))

        OutlinedTextField(value = reminder, onValueChange = {
            reminder = it
        },
            label = {
                Text(text = "reminder1")

            }
        )
        OutlinedTextField(value = reminder2, onValueChange = {
            reminder2 = it
        },
            label = {
                Text(text = "reminder2")

            }
        )
        OutlinedTextField(value = reminder3, onValueChange = {
            reminder3 = it
        },
            label = {
                Text(text = "reminder3")

            }
        )

        OutlinedTextField(value = reminder4, onValueChange = {
            reminder4 = it
        },
            label = {
                Text(text = "reminder4")

            }
        )


        Button(onClick = {
            // Save reminders to Fire store
            val userId = "your_user_id" // You should get the current user's ID
            val remindersList =
                listOf(reminder, reminder2, reminder3, reminder4).filter { it.isNotBlank() }
            remindersList.forEach {
                val newReminder = hashMapOf(
                    "Text" to it,
                    "daysUntil" to "5"
                ) // Assuming default daysUntil as 5 for now
                db.collection("users").document(userId).collection("reminders").add(newReminder)
            }
            reminder = ""
            reminder2 = ""
            reminder3 = ""
            reminder4 = ""
        }) {
            Text(text = "Add Reminders")
        }

        BackButton(navController)

        Button(onClick = { navController.navigate("menu") }) {
            Text(text = "Go to menu")
        }
    }
}

