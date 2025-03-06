package com.example.newproject3.Pages

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.newproject3.authViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RemindersPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: authViewModel
) {

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current
    val db: FirebaseFirestore = Firebase.firestore

    var taskText by remember { mutableStateOf("") }
    var daysUntil by remember { mutableStateOf("") }
    var tasks by remember { mutableStateOf(listOf<Pair<String, Long>>()) }
    val userId = "your_user_id" // Replace with the actual user ID

    // Fetch tasks from Firestore
    LaunchedEffect(userId) {
        db.collection("User").document(userId).collection("Task")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    tasks = snapshot.documents.mapNotNull { doc ->
                        val text = doc.getString("Text")
                        val dateAdded = doc.getString("dateAdded")?.let { LocalDate.parse(it) }
                        if (text != null && dateAdded != null) {
                            val daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), dateAdded).coerceAtLeast(0)
                            text to daysLeft
                        } else null
                    }
                }
            }
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Tasks", fontSize = 32.sp)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = taskText,
            onValueChange = { taskText = it },
            label = { Text(text = "Task") }
        )
        OutlinedTextField(
            value = daysUntil,
            onValueChange = { daysUntil = it },
            label = { Text(text = "Days Until Completion") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            val days = daysUntil.toIntOrNull()
            if (taskText.isNotBlank() && days != null) {
                val taskData = hashMapOf(
                    "Text" to taskText,
                    "dateAdded" to LocalDate.now().plusDays(days.toLong()).toString()
                )
                db.collection("User").document(userId).collection("Task").add(taskData)
                taskText = ""
                daysUntil = ""
            } else {
                Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text(text = "Add Task")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(tasks.size) { index ->
                val (task, daysLeft) = tasks[index]
                Text(text = "$task - $daysLeft days left", modifier = Modifier.padding(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("CarDetailsPage/$userId") }) {
            Text(text = "Go to Car Details")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Back")
        }
    }
}


/*  // Navigate if authenticated
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
      Text(text = "Tasks", fontSize = 32.sp)

      Spacer(modifier = modifier.height(16.dp))

      OutlinedTextField(value = task1, onValueChange = { task1 = it }, label = { Text(text = "Task 1") })
      OutlinedTextField(value = task2, onValueChange = { task2 = it }, label = { Text(text = "Task 2") })
      OutlinedTextField(value = task3, onValueChange = { task3 = it }, label = { Text(text = "Task 3") })
 //     OutlinedTextField(value = task4, onValueChange = { task4 = it }, label = { Text(text = "Task 4") })
/*

        Button(onClick = {
            val userId = "your_user_id" // TODO: Replace with actual logged-in user ID
            val tasksList = listOf(task1, task2, task3, task4).filter { it.isNotBlank() }

            if (userId.isNotBlank()) {
                tasksList.forEach { task ->
                    val newTask = hashMapOf(
                        "Text" to task,
                        "daysUntil" to "5" // Default value
                    )
                    db.collection("User").document(userId).collection("Task").add(newTask)
                }

                // Reset fields after adding
                task1 = ""
                task2 = ""
                task3 = ""
                task4 = ""

                Toast.makeText(context, "Tasks added!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "User not found!", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text(text = "Add Tasks")
        }
    }
}
*/

 */