package com.espresso.machine.view

import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.espresso.machine.R
import com.espresso.machine.components.HeaderText
import com.espresso.machine.components.SwipableCartItem
import com.espresso.machine.model.entity.Order
import com.espresso.machine.model.entity.OrderItem
import com.espresso.machine.model.repository.CardRepository
import com.espresso.machine.model.repository.OrderRepository
import com.espresso.machine.model.repository.UserRepository
import com.espresso.machine.viewModel.CheckoutViewModel
import com.espresso.machine.viewModel.CheckoutViewModelFactory
import com.espresso.machine.viewModel.OrderState
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CheckoutScreen(
    orderRepository: OrderRepository,
    userRepository: UserRepository,
    cardRepository: CardRepository,
    userEmailAddress: String,
    onOrderItemUpdate: (OrderItem, Order) -> Unit, // navigation to update order
    navigateToHome: (Order) -> Unit,
    navigateToAddCard: () -> Unit,
    navigateToOrderStatusTracker: (Order) -> Unit
) {
    val viewModel = viewModel<CheckoutViewModel>(
        factory = CheckoutViewModelFactory(
            orderRepository,
            userRepository,
            cardRepository,
            userEmailAddress
        )
    )
    val openDialog = remember { mutableStateOf(false) }
    val order = viewModel.order!!
    val orderItem = viewModel.state.orderItem

    Scaffold {
        Checkout(
            navigateToHome = { navigateToHome(order) },
            state = viewModel.state,
            onOrderItemRemove = viewModel::onOrderItemRemove,
            onOrderItemUpdate = {
                if (orderItem != null) {
                    onOrderItemUpdate(orderItem, order)
                }
            },
            onIncreaseQuantity = viewModel::increaseQuantity,
            onDecreaseQuantity = viewModel::decreaseQuantity,
            onOrderItemFocus = viewModel::onOrderItemFocus,
            navigateToAddCard = { navigateToAddCard() },
            onCheckoutConfirmed = { viewModel.update() },
            availableCard = viewModel.getAvailableCard(),
            openDialog = openDialog,
            navigateToOrderStatusTracker = { navigateToOrderStatusTracker(order) }
        )
    }


}

@Composable
fun Checkout(
    navigateToHome: () -> Unit,
    navigateToAddCard: () -> Unit,
    state: OrderState,
    availableCard: String?,
    navigateToOrderStatusTracker: () -> Unit,
    onOrderItemRemove: (OrderItem) -> Unit,
    onOrderItemUpdate: () -> Unit,
    onCheckoutConfirmed: () -> Unit,
    onOrderItemFocus: (OrderItem) -> Unit,
    onIncreaseQuantity: (OrderItem) -> Unit,
    onDecreaseQuantity: (OrderItem) -> Unit,
    openDialog: MutableState<Boolean>
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp)
    ) {

        // checkout header and backward navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically // Centers child composables vertically within the Row
        ) {
            Icon(
                modifier = Modifier.clickable { navigateToHome() },
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null
            )

            HeaderText(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 65.dp),
                text = "Checkout"
            )
        }

        /**
         * Placing the column on top of the header because boxes render differently from, they don't stack composables on top of eachother
         */

        if (state.orderItems.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 60.dp)
                    .fillMaxSize(),
                contentPadding = PaddingValues(vertical = 12.dp),
            ) {
                itemsIndexed(
                    items = state.orderItems,
                    key = { _, orderItem -> orderItem.id }
                ) { _, orderItem ->
                    Box(modifier = Modifier.animateItem(fadeOutSpec = tween(600))) {
                        SwipableCartItem(
                            modifier = Modifier.clickable {
                                onOrderItemFocus(orderItem)
                                onOrderItemUpdate()
                            },
                            orderItem = orderItem,
                            onRemove = { onOrderItemRemove(orderItem) },
                            onIncreaseQuantity = { onIncreaseQuantity(orderItem) },
                            onDecreaseQuantity = { onDecreaseQuantity(orderItem) },
                            state = state
                        )
                    }
                }
            }
        }

        if (state.orderItems.isEmpty()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "You don't have any items in your cart yet"
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {

            // If cart is not empty user has the option managing their cards and confirm checkout
            if (state.orderItems.isNotEmpty()) {
                CardOptions(availableCard = availableCard, navigateToAddCard = navigateToAddCard)

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Total", fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    Text("£${state.totalPrice}", fontSize = 25.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onCheckoutConfirmed()
                        openDialog.value = true

                    }
                ) {
                    Text(text = "Confirm Checkout")
                }
            }
            Button(modifier = Modifier.fillMaxWidth(), onClick = { navigateToHome() }) {
                Text(text = "Add Items")

            }

            if (openDialog.value) {
                LaunchedEffect(Unit) {
                    // Delay for 2 seconds
                    delay(2000)
                    // Dismiss dialog and trigger navigation
                    openDialog.value = false
                    navigateToOrderStatusTracker()
                }
            }

            when {
                openDialog.value -> {
                    PurchaseConfirmationNotification { openDialog.value = false }
                }
            }

        }

    }
}

/**
 * Card that shows any available cards that the user currently
 */
@Composable
fun CardOptions(
    modifier: Modifier = Modifier,
    availableCard: String? = null,
    navigateToAddCard: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(110.dp)
            .background(
                color = Color(0xFFA05E26),
                shape = RoundedCornerShape(size = 20.dp)
            )
            .padding(12.dp)
    ) {
        // Payment header
        Text(
            modifier = Modifier.padding(horizontal = 6.dp),
            text = "Payment",
            style = TextStyle(
                fontSize = 27.sp,
                //fontFamily = FontFamily(Font(R.font.inter)),
                fontWeight = FontWeight(400),
                color = Color(0xFFFFFFFF),
            )
        )

        Spacer(modifier = modifier.height(4.dp))

        // Card option shown here
        Row(
            modifier = modifier
                .align(Alignment.CenterStart)
                .padding(horizontal = 6.dp)
        ) {

            if (availableCard != null) {
                Icon(
                    painter = painterResource(R.drawable.logos_visa),
                    contentDescription = "Visa card logo"
                )

                Spacer(modifier = modifier.width(8.dp))

                Text(
                    text = "Card ending $availableCard",
                    style = TextStyle(
                        fontSize = 15.sp,
                        // fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(500),
                        color = Color(0xFFFFFFFF),
                    )
                )

            } else {

                Text(
                    text = "You don't have any active cards",
                    style = TextStyle(
                        fontSize = 15.sp,
                        // fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(500),
                        color = Color(0xFFFFFFFF),
                    )
                )
            }
        }

        Row(
            modifier = modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .clickable { navigateToAddCard() }
                .padding(horizontal = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Add a new card",
                style = TextStyle(
                    fontSize = 15.sp,
                    //  fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFFFFFFFF),
                )
            )

            Icon(
                painter = painterResource(R.drawable.arrow_right),
                contentDescription = "Arrow right"
            )
        }
    }
}

@Composable
fun PurchaseConfirmationNotification(modifier: Modifier = Modifier, onDismissRequest: () -> Unit) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties()
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = modifier.size(80.dp),
                    painter = painterResource(R.drawable.check_circle_svgrepo_com),
                    contentDescription = null
                )

                Spacer(modifier = modifier.height(10.dp))

                Text(
                    text = "Payment Confirmed! We're processing your order now.",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewDialog() {
    val openDialog = remember { mutableStateOf(true) }
    PurchaseConfirmationNotification { openDialog.value }
}

@Preview(showBackground = true)
@Composable
fun PreviewCardOptions() {
    CardOptions(navigateToAddCard = {})
}

/*@Preview(showSystemUi = true)
@Composable
fun PreviewCheckout() {
    TheEspressoMachineTheme {
        Checkout()
    }
}*/
