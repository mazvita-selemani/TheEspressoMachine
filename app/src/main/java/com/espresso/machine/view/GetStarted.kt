package com.espresso.machine.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.espresso.machine.R

@Composable
fun GetStarted(
    onStart : () -> Unit
){
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.TopCenter,
            painter = painterResource(R.drawable.coffee_welcome_page),
            contentDescription = null
        )

        Column(
            modifier = Modifier.align(Alignment.BottomCenter)
            .background(
            color = Color(0xFFDBAA81),
            shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
            ).fillMaxWidth().fillMaxHeight(.55f),
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Box(
                modifier = Modifier
                    .clickable{}
                    .padding(4.dp)
                    .size(97.dp)
                    .clip(shape = CircleShape)
                    .background(color = Color.White, shape = CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(R.drawable.material_symbols_light_coffee),
                    contentDescription = "Back to main menu"
                )
            }

            Text(
                text = "THE ESPRESSO MACHINE",
                style = TextStyle(
                    fontSize = 30.sp,
                   // fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF000000),
                )
            )

            Text(
                text = "we have nice coffee",
                style = TextStyle(
                    fontSize = 24.sp,
                    //fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF000000),
                )
            )

            Button(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
                    .height(60.dp).clip(shape = RoundedCornerShape(size = 20.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                onClick = onStart
            ) {
                Text("Get Started")
            }


        }
    }

}

/*
@Preview(showSystemUi = true)
@Composable
fun PreviewGetStarted(){
    GetStarted()
}*/
