package com.example.newproject3.Pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newproject3.authViewModel


@Composable
fun CardetailsPage(modifier: Modifier = Modifier, navController: NavController,authViewModel: authViewModel){

    var carname by remember {
        mutableStateOf("")
    }
    var nextreminder by remember {
        mutableStateOf("")
    }

    val authState = authViewModel.authState.observeAsState()

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(text = "Your Car Details", fontSize = 32)

        Spacer(modifier = modifier.height(16.dp))
        OutlinedTextField(value = )
}

