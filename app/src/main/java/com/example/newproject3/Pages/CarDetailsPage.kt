package com.example.newproject3.Pages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.example.newproject3.User

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CarDetailsPage(userId: String, navController: NavController) {
    val db = FirebaseFirestore.getInstance()

    var carName by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var engine by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var mileage by remember { mutableStateOf("") }
    var lastServiceDate by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        db.collection("User").document(userId).get()
            .addOnSuccessListener { document ->
                val user = document.toObject(User::class.java)
                user?.let {
                    brand = it.Brand
                    carName = it.CarName
                    engine = it.engine
                    year = it.Year.toString()
                    mileage = it.Mileage.toString()
                    lastServiceDate = it.LastServiceDate
                }
            }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "bubbles")//יוצר אנימציה שלא נגמרת
    val alpha by infiniteTransition.animateFloat(//ערך השקיפות
        initialValue = 0.3f, //שינוי הדרגתי
        targetValue = 1f,
        animationSpec = infiniteRepeatable( //לא מפסיק
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ), label = "bubbleAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E)),
        contentAlignment = Alignment.Center
    ) {
        // Decorative animated background bubbles (closer together)
        listOf(
            OffsetBubble(50.dp, 60.dp),
            OffsetBubble(180.dp, 50.dp),
            OffsetBubble(100.dp, 180.dp),
            OffsetBubble(200.dp, 230.dp),
            OffsetBubble(80.dp, 320.dp),
            OffsetBubble(160.dp, 370.dp)
        ).forEach { offset ->
            Surface(//הגדרת של הבועות
                modifier = Modifier
                    .size(170.dp)
                    .offset(x = offset.x, y = offset.y)//מיקום
                    .alpha(alpha)//רמת שקיפות
                    .zIndex(0f),
                shape = CircleShape,
                border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF8A2BE2)),
                color = Color.Transparent
            ) {}
        }

        // Foreground car details
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .zIndex(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Car Details", fontSize = 28.sp, color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            DetailBubble("Brand", brand)
            DetailBubble("Car Name", carName)
            DetailBubble("Engine", engine)
            DetailBubble("Year", year)
            DetailBubble("Mileage", "$mileage km")
            DetailBubble("Last Service", lastServiceDate)
        }
    }
}

@Composable
fun DetailBubble(label: String, value: String) {
    Surface(
        modifier = Modifier
            .padding(vertical = 6.dp)
            .fillMaxWidth(0.8f),
        shape = CircleShape,
        color = Color(0xFF8A2BE2).copy(alpha = 0.2f),
        border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF8A2BE2))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, color = Color.White, fontSize = 16.sp)
        }
    }
}


data class OffsetBubble(val x: Dp, val y: Dp)