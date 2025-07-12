package com.espresso.machine.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.espresso.machine.R

val itemSpacing = 6.dp

@Composable
fun MenuItem(
    modifier: Modifier = Modifier,
    itemName: String = "Default Name",
    itemPricing: String = "£0.00",
    painter: Painter? = null,
    onItemClick: () -> Unit
){
    Column(
        modifier = modifier
            .padding(4.dp)
            .clickable { onItemClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
       // verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = modifier
                .width(120.dp)
                .height(150.dp)
                .background(color = Color(0xFFF8BEB0), shape = RoundedCornerShape(10.dp))
        ) {
            // TODO add liked items state
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = null,
                modifier = modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
            )

            if (painter != null) {
                Image(
                    painter,
                    contentDescription = null,
                    modifier = modifier
                        .align(Alignment.Center)
                        .padding(2.dp)
                )
              //  return
            }

            if( painter == null) {
                Text(
                    text = "Could not load the image",
                    modifier = modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(itemSpacing))

        Text(
            text = itemName
        )

        Spacer(Modifier.height(itemSpacing))

        Text(
            text = itemPricing
        )

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMenuItem(){
    MenuItem(
        painter = painterResource(R.drawable.latte),
        onItemClick = {}
    )
}
