package com.espresso.machine.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.espresso.machine.R
import com.espresso.machine.components.HeaderText
import com.espresso.machine.model.entity.OrderStatus
import com.espresso.machine.model.repository.OrderRepository
import com.espresso.machine.viewModel.OrderStatusTrackerViewModel
import com.espresso.machine.viewModel.OrderStatusTrackerViewModelFactory
import com.espresso.machine.viewModel.OrderTrackerState
import kotlinx.coroutines.delay

@Composable
fun OrderStatusTrackerScreen(
    navigateToHome: () -> Unit,
    orderRepository: OrderRepository,
    orderId: Int
) {
    val viewModel = viewModel<OrderStatusTrackerViewModel>(
        factory = OrderStatusTrackerViewModelFactory(
            orderRepository
        )
    )

    OrderStatusIndicator(
        viewModel = viewModel,
        orderId = orderId,
        state = viewModel.state,
        navigateToHome = { navigateToHome() }
    )
}

@Composable
fun OrderStatusIndicator(
    viewModel: OrderStatusTrackerViewModel,
    orderId: Int,
    state: OrderTrackerState,
    navigateToHome: () -> Unit
) {

    LaunchedEffect(state.status) {
        when (state.status) {
            OrderStatus.AWAITING_CONFIRMATION -> {
                delay(10_000L) // 10 seconds
                viewModel.onOrderPreparing(orderId)
            }

            OrderStatus.PREPARING -> {
                delay(10_000L) // 10 seconds
                viewModel.onOrderOnItsWay(orderId)
            }

            OrderStatus.ON_ITS_WAY -> {
                delay(10_000L) // 10 seconds
                viewModel.onOrderComplete(orderId)
            }

            OrderStatus.DONE -> {
                navigateToHome()
            }

            else -> {} // do nothing
        }
    }

    // Display UI elements based on the current status
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        HeaderText(
            text = "Order Status",
            modifier = Modifier,
        )

        CircularProgressIndicator(
            modifier = Modifier.size(100.dp),
            strokeWidth = 8.dp,
            color = when (state.status) {
                OrderStatus.AWAITING_CONFIRMATION -> Color.Gray
                OrderStatus.PREPARING -> Color.Blue
                OrderStatus.ON_ITS_WAY -> Color.Green
                OrderStatus.DONE -> Color.Cyan
                else -> Color.Blue
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display icon based on order status
        Icon(
            painter = when (state.status) {
                OrderStatus.AWAITING_CONFIRMATION -> painterResource(R.drawable.timer)
                OrderStatus.PREPARING -> painterResource(R.drawable.coffeemaker)
                OrderStatus.ON_ITS_WAY -> painterResource(R.drawable.ready_to_serve_coffee)
                OrderStatus.DONE -> painterResource(R.drawable.check_circle_svgrepo_com)
                else -> painterResource(R.drawable.check_circle_svgrepo_com)
            },
            contentDescription = null,
            tint = when (state.status) {
                OrderStatus.AWAITING_CONFIRMATION -> Color.Gray
                OrderStatus.PREPARING -> Color.Blue
                OrderStatus.ON_ITS_WAY -> Color.Green
                OrderStatus.DONE -> Color.Cyan
                else -> Color.Transparent
            },
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Display text based on order status
        Text(
            text = when (state.status) {
                OrderStatus.AWAITING_CONFIRMATION -> "Awaiting Confirmation"
                OrderStatus.PREPARING -> "Preparing"
                OrderStatus.ON_ITS_WAY -> "On Its Way"
                OrderStatus.DONE -> "Order Complete"
                else -> ""
            },
            fontSize = 18.sp
        )
    }
}




