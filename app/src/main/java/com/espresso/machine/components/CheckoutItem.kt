package com.espresso.machine.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.espresso.machine.model.entity.OrderItem
import com.espresso.machine.viewModel.OrderState

@Composable
fun CheckoutItem(
    modifier: Modifier = Modifier,
    orderItem: OrderItem,
    state : OrderState,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    painter: Painter? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(110.dp)
            .background(color = Color(0xFFE8C9B0), shape = RoundedCornerShape(size = 25.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
           // horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = modifier
                    .width(74.dp)
                    .height(84.dp)
                    .background(
                        color = Color(0xFFDBAA81),
                        shape = RoundedCornerShape(size = 10.dp)
                    )
            ) {
                if (orderItem.foodResId != null) {
                    Image(
                        painter = painterResource(orderItem.foodResId),
                        contentDescription = null,
                        modifier = modifier
                            .align(Alignment.Center)
                            .padding(2.dp)
                    )
                }

                if( orderItem.foodResId == null) {
                    Text(
                        text = "Could not load the image",
                        modifier = modifier.align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )
                }

            }

            Spacer(modifier = modifier.width(10.dp))

            Column(
                modifier = modifier.weight(1f)
            ) {

                Text(text = orderItem.foodName!!, fontSize = 20.sp)

                if (orderItem.size != null && orderItem.milkType != null){
                    Text(
                        text = "${orderItem.size},${orderItem.milkType}",
                        fontSize = 15.sp
                    )
                }
                if (orderItem.size == null && orderItem.milkType == null){
                    Text(
                        text = "",
                        fontSize = 15.sp
                    )
                }
                Text(text = "£${ orderItem.itemPriceTotal }", fontSize = 18.sp)
            }

            Spacer(modifier = modifier.width(35.dp))

            Row(
                modifier = modifier.padding(6.dp).width(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    modifier = modifier
                        .width(26.dp)
                        .height(30.49773.dp),
                    shape = RoundedCornerShape(
                        topStart = 10.dp,
                        topEnd = 0.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 10.dp
                    ),
                    onClick = onDecreaseQuantity
                ) {
                    Text("-")
                }

                Spacer(Modifier.width(12.dp))

                Text("${orderItem.quantity}")

                Spacer(Modifier.width(12.dp))

                Button(
                    modifier = modifier
                        .width(26.dp)
                        .height(30.49773.dp),
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 10.dp,
                        bottomStart = 10.dp,
                        bottomEnd = 0.dp
                    ),
                    onClick = onIncreaseQuantity
                ) {
                    Text( textAlign = TextAlign.Center, color = Color.White, text = "+")
                }
            }
        }


    }

}

/*
@Preview(showBackground = true)
@Composable
fun PreviewCheckoutItem() {
    CheckoutItem(food = Food(9.00, "Toothpaste"))
}*/
