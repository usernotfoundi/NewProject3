package com.example.newproject3.pages

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import utils.fetchNearbyGasStations

@Composable
fun GasStationMapScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    val context = LocalContext.current
    val fusedLocationProviderClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }//מקבל את ספק המיקום של GOOGLE ונקבל את המיקום האחרון של המשתמש

    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var gasStations by remember { mutableStateOf<List<Pair<String, LatLng>>>(emptyList()) }
    val cameraPositionState = rememberCameraPositionState()
    val scope = rememberCoroutineScope()

    val locationPermissionGranted = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(//מבקש הרשאת מיקום
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        locationPermissionGranted.value = granted
    }

    LaunchedEffect(Unit) {//שליפת מיקום המשתמש
        if (!locationPermissionGranted.value) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            val location: Location? = fusedLocationProviderClient.lastLocation.await()
            location?.let {
                val latLng = LatLng(it.latitude, it.longitude)
                userLocation = latLng
                cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 14f)

                val stations = fetchNearbyGasStations(it.latitude, it.longitude)
                gasStations = stations//מביאה תחנות דלק בסביבה לפי הקורדינאטות
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true),//מראה את מקיום המשתמש
            uiSettings = MapUiSettings(zoomControlsEnabled = true)//מוסיף כפתורי זום
        ) {
            userLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "You are here"
                )
            }

            gasStations.forEach { (name, position) ->
                Marker(
                    state = MarkerState(position = position),
                    title = name
                )
            }
        }

        FloatingActionButton(
            onClick = {
                scope.launch {
                    if (locationPermissionGranted.value) {
                        val location: Location? = fusedLocationProviderClient.lastLocation.await()
                        location?.let {
                            val latLng = LatLng(it.latitude, it.longitude)
                            userLocation = latLng
                            cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 14f)
                        }
                    } else {
                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("📍")
        }
    }
}