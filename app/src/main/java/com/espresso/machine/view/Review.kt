package com.espresso.machine.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ReviewScreen(
    navigateToHome: () -> Unit
) {
    var value by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Tell us about your experience?",
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = value,
            onValueChange = { newValue -> value = newValue },
            modifier = Modifier
                .fillMaxWidth()
                .height(136.dp)
                .background(
                    color = Color(0xFFF4F4F4),
                    shape = RoundedCornerShape(size = 20.dp)
                ),
            shape = RoundedCornerShape(size = 20.dp),
            colors = OutlinedTextFieldDefaults.colors()
        )

        Spacer(modifier = Modifier.height(10.dp))

        SatisfactionRating()

        Spacer(modifier = Modifier.height(20.dp))

        Button(modifier = Modifier.fillMaxWidth(), onClick = { navigateToHome() }) {
            Text("Submit Rating")
        }

        TextButton(onClick = { navigateToHome() }) {
            Text("Go Back Home")
        }

    }

}

@Composable
private fun SatisfactionRating() {
    // Define satisfaction emojis
    val satisfactionEmojis = listOf(
        "😞", // Not satisfied
        "😐", // Neutral
        "🙂", // Somewhat satisfied
        "😊", // Satisfied
        "😍"  // Extremely satisfied
    )

    // State to track selected rating index
    var selectedRating by remember { mutableIntStateOf(-1) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Display each emoji as a selectable item
        satisfactionEmojis.forEachIndexed { index, emoji ->
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        color = if (index == selectedRating) Color(0xFF8B4513) else Color.Gray
                    ) // Brown if selected, else grey
                    .clickable { selectedRating = index }, // Update selected index on click
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emoji,
                    fontSize = 24.sp,
                    color = Color.White
                )
            }
        }
    }
}


/*
@Preview(showSystemUi = true)
@Composable
fun PreviewTextField(){

TheEspressoMachineTheme {
    CustomTextField({} )

}
}
*/
