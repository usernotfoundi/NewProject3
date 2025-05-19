package com.example.newproject3.Pages

import android.app.DatePickerDialog
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.newproject3.authViewModel
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RemindersPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: authViewModel
) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    val recurrenceOptions = listOf("None", "Every 7 days", "Monthly")
    var selectedRecurrence by remember { mutableStateOf(recurrenceOptions[0]) }

    var taskText by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf<LocalDate?>(null) }
    var tasks by remember { mutableStateOf(listOf<Triple<String, Long, String>>()) }
    var editingTaskId by remember { mutableStateOf<String?>(null) }

    val userId = "bihhAIJebLUfuXB1V6sz"

    val infiniteTransition = rememberInfiniteTransition(label = "bubbles")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ), label = "bubbleAlpha"
    )

    LaunchedEffect(userId) {
        db.collection("User").document(userId).collection("Task")
            .addSnapshotListener { snapshot, _ ->
                val newTasks = mutableListOf<Triple<String, Long, String>>()
                snapshot?.documents?.forEach { doc ->
                    val text = doc.getString("Text") ?: return@forEach
                    val date = doc.getString("dateAdded")?.let { LocalDate.parse(it) } ?: return@forEach
                    val recurrence = doc.getString("recurrence") ?: "None"
                    val daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), date)

                    if (daysLeft > 0) {
                        newTasks.add(Triple(text, daysLeft, doc.id))
                    } else {
                        when (recurrence) {
                            "Every 7 days" -> {
                                val newDate = date.plusDays(7)
                                db.document(doc.reference.path).update("dateAdded", newDate.toString())
                            }
                            "Monthly" -> {
                                val newDate = date.plusMonths(1)
                                db.document(doc.reference.path).update("dateAdded", newDate.toString())
                            }
                            else -> {
                                db.document(doc.reference.path).delete()
                            }
                        }
                    }
                }
                tasks = newTasks
            }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E)),
        contentAlignment = Alignment.Center
    ) {
        listOf(
            OffsetBubble(40.dp, 70.dp),
            OffsetBubble(180.dp, 40.dp),
            OffsetBubble(100.dp, 200.dp),
            OffsetBubble(240.dp, 260.dp),
            OffsetBubble(60.dp, 320.dp),
            OffsetBubble(150.dp, 380.dp)
        ).forEach { offset ->
            Surface(
                modifier = Modifier
                    .size(150.dp)
                    .offset(x = offset.x, y = offset.y)
                    .alpha(alpha)
                    .zIndex(0f),
                shape = CircleShape,
                border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF8A2BE2)),
                color = Color.Transparent
            ) {}
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .zIndex(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Reminders", fontSize = 28.sp, color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = taskText,
                onValueChange = { taskText = it },
                label = { Text("Reminder text") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            val today = LocalDate.now()
            val datePickerDialog = remember {
                DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        dueDate = LocalDate.of(year, month + 1, dayOfMonth)
                    },
                    today.year, today.monthValue - 1, today.dayOfMonth
                )
            }

            TextButton(onClick = { datePickerDialog.show() }) {
                Text(dueDate?.toString() ?: "Select Due Date")
            }

            Spacer(modifier = Modifier.height(8.dp))

            var expanded by remember { mutableStateOf(false) }

            Box {
                TextButton(onClick = { expanded = true }) {
                    Text("Repeat: $selectedRecurrence")
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    recurrenceOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedRecurrence = option
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                if (taskText.isBlank() || dueDate == null) {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val taskData = hashMapOf(
                    "Text" to taskText,
                    "dateAdded" to dueDate.toString(),
                    "recurrence" to selectedRecurrence
                )

                if (editingTaskId != null) {
                    db.collection("User").document(userId).collection("Task")
                        .document(editingTaskId!!)
                        .update(taskData as Map<String, Any>)
                    editingTaskId = null
                } else {
                    db.collection("User").document(userId).collection("Task")
                        .add(taskData)
                }

                taskText = ""
                dueDate = null
                selectedRecurrence = recurrenceOptions[0]
            }) {
                Text(if (editingTaskId == null) "Add Reminder" else "Update Reminder")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(tasks) { (text, daysLeft, docId) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C4A)) // Dark purple-gray
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text, fontSize = 18.sp, color = Color.White)
                            Text("Due in $daysLeft days", color = Color.LightGray)

                            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                                TextButton(onClick = {
                                    taskText = text
                                    dueDate = LocalDate.now().plusDays(daysLeft)
                                    editingTaskId = docId
                                }) {
                                    Text("Edit", color = Color.White)
                                }
                                TextButton(onClick = {
                                    db.collection("User").document(userId).collection("Task").document(docId).delete()
                                }) {
                                    Text("Delete", color = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}


