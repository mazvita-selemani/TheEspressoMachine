package com.espresso.machine.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.espresso.machine.R
import com.espresso.machine.model.entity.OrderItem
import com.espresso.machine.viewModel.OrderState


// this composable wraps the checkout items and allows them to be swipable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipableCartItem(
    orderItem: OrderItem,
    state: OrderState,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    modifier: Modifier = Modifier,
    onRemove: (OrderItem) -> Unit
) {
    val context = LocalContext.current
    var isRemoved by remember { mutableStateOf(false) }
    val currentItem by rememberUpdatedState(orderItem)
    val dismissState = rememberSwipeToDismissBoxState(
        // confirm value change returns true on swipe end to start condition only, otherwise the list does not change
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.EndToStart -> {
                    onRemove(currentItem)
                    isRemoved = true
                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
                }

                SwipeToDismissBoxValue.StartToEnd -> return@rememberSwipeToDismissBoxState false
                SwipeToDismissBoxValue.Settled -> return@rememberSwipeToDismissBoxState false
            }

            return@rememberSwipeToDismissBoxState true
        },
        positionalThreshold = { it * .25f }
    )


    SwipeToDismissBox(
        modifier = modifier.padding(vertical = 6.dp),
        state = dismissState,
        backgroundContent = { DismissBackground(dismissState) },
        content = {
            CheckoutItem(
                orderItem = orderItem,
                onDecreaseQuantity = onDecreaseQuantity,
                onIncreaseQuantity = onIncreaseQuantity,
                state = state
            )
        },
        enableDismissFromStartToEnd = false
    )

}

/**
 * This is a background composable
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(
    dismissState: SwipeToDismissBoxState
) {

    val color = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> Color.Transparent
        SwipeToDismissBoxValue.Settled -> Color.Transparent
        SwipeToDismissBoxValue.StartToEnd -> Color.Transparent
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(horizontal = 20.dp, vertical = 8.dp),
        contentAlignment = Alignment.CenterEnd
    ) {

        Icon(
            painter = painterResource(R.drawable.delete_button),
            contentDescription = "Delete Cart Item"
        )

    }
}

/*
@Preview(showSystemUi = true)
@Composable
fun PreviewDismissBackground() {

    SwipableCartItem(modifier = Modifier.padding(vertical = 40.dp), food = Food(90.00, "Fanta"), onRemove = {})

}*/
