package com.example.newproject3.Pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.newproject3.authViewModel



@Composable
fun NewUserPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: authViewModel,

) {
    var name by remember { mutableStateOf("") }
    var carBrand by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var daysUntil by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFE6CCFF)) // Light Purple Background
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BackButton(navController)


        Text(text = "New User", fontSize = 24.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(20.dp))

        // Name field
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )


        // Car Brand field
        TextField(
            value = carBrand,
            onValueChange = { carBrand = it },
            label = { Text("Car Brand") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )


        // Model field
        TextField(
            value = model,
            onValueChange = { model = it },
            label = {Text("Model")},
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        TextField(
            value = year,
            onValueChange = { year = it },
            label = {Text("Year")},
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        TextField(
            value = daysUntil,
            onValueChange = {daysUntil = it},
            label = {Text("daysUntil")},
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)

        )


        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            // Pass data to UsersPage via navigation
            navController.navigate("users/${name}/${carBrand}/${model}/${year}/${daysUntil}")
        }) {
            Text(text = "Submit")
        }
    }
}

@Composable
fun BackButton(navController: NavController) {
    IconButton(onClick = { navController.navigateUp() }) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
    }

}



