package com.espresso.machine.view

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.espresso.machine.R
import com.espresso.machine.components.HeaderText
import com.espresso.machine.components.MenuItem
import com.espresso.machine.components.SearchBar
import com.espresso.machine.model.entity.Food
import com.espresso.machine.model.entity.OrderStatus
import com.espresso.machine.model.entity.User
import com.espresso.machine.model.repository.FoodRepository
import com.espresso.machine.model.repository.OrderRepository
import com.espresso.machine.model.repository.UserRepository
import com.espresso.machine.viewModel.MainMenuState
import com.espresso.machine.viewModel.MainMenuViewModel
import com.espresso.machine.viewModel.MainMenuViewModelFactory
import kotlinx.coroutines.delay

val categoryButtons = mapOf(
    "Hot Drinks" to "HOT_DRINK",
    "Cold Drinks" to "COLD_DRINK",
    "Baked Goods" to "BAKED_GOODS"
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainMenuScreen(
    foodRepository: FoodRepository,
    userRepository: UserRepository,
    orderRepository: OrderRepository,
    onItemClick: (Int) -> Unit,
    userName: String,
    orderId: Int? = null,
    navigateToCheckout: (User) -> Unit,
    navigateToReviewPage: () -> Unit
) {
    val viewModel = viewModel<MainMenuViewModel>(
        factory = MainMenuViewModelFactory(
            foodRepository,
            userRepository,
            orderRepository
        )
    )
    val itemsByCategory =
        viewModel.loadItemsByCategory(viewModel.state.category.name).observeAsState()
    val user by lazy { viewModel.findUserByEmail(userName) }
    val order by lazy { orderId?.let { viewModel.getOrderById(it) } }


    // this a dialog that will pop up after the user has finished their order
    val openDialog = remember { mutableStateOf(false) }

    if (order?.status == OrderStatus.DONE) {
        LaunchedEffect(Unit) {
            delay(5000)
            openDialog.value = true
        }

        ReviewPrompt(
            onDismissRequest = { openDialog.value = false },
            navigateToReviewPage = navigateToReviewPage
        )
    }



    Scaffold {
        MainMenu(
            userName = user.firstName!!,
            state = viewModel.state,
            onCategoryChange = viewModel::onCategoryChange,
            onSearchChange = viewModel::onSearchChange,
            loadItemsByCategory = itemsByCategory.value,
            onItemClick = onItemClick,
            navigateToCheckout = { navigateToCheckout(user) }
        )
    }

}

@Composable
private fun MainMenu(
    userName: String,
    state: MainMenuState,
    onCategoryChange: (String) -> Unit,
    onSearchChange: (String) -> Unit,
    loadItemsByCategory: List<Food>?,
    onItemClick: (Int) -> Unit,
    navigateToCheckout: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HeaderText(
                text = "Hi, $userName \uD83D\uDC4B",
                modifier = Modifier.align(Alignment.Start)
            )
            Text(
                text = "we have nice coffee, have some",
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.Start)
            )

            SearchBar(value = state.search, onValueChange = { onSearchChange(it) })

            HeaderText(text = "Browse our menu...", modifier = Modifier)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                categoryButtons.forEach { item ->
                    Button(onClick = { onCategoryChange(item.value) }) {
                        Text(item.key, fontSize = 13.sp)
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // if list empty do nothing
                if (loadItemsByCategory.isNullOrEmpty()) return@LazyColumn

                items(items = loadItemsByCategory.filter {
                    it.name?.contains(state.search, ignoreCase = true) == true
                }, key = { it.id })
                { food ->

                    if (food.resId != null) {
                        food.name?.let {
                            MenuItem(
                                itemName = it,
                                itemPricing = food.price.toString(),
                                painter = painterResource(food.resId),
                                onItemClick = { onItemClick(food.id) }
                            )
                        }
                    }

                    if (food.resId == null) {
                        food.name?.let {
                            MenuItem(
                                itemName = it,
                                itemPricing = food.price.toString(),
                                onItemClick = { onItemClick(food.id) }
                            )
                        }
                    }

                }

            }

        }

        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = Color(0xFF000000))
                .height(100.dp)
                .align(Alignment.BottomCenter)
        ) {
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.mdi_home),
                        contentDescription = null
                    )
                },
                selected = false,
                onClick = {}
            )

            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.mdi_heart),
                        contentDescription = null
                    )
                },
                selected = false,
                onClick = {}
            )

            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ion_bag_sharp),
                        contentDescription = null
                    )
                },
                selected = false,
                onClick = { navigateToCheckout() }
            )


        }
    }
}


@Composable
private fun ReviewPrompt(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    navigateToReviewPage: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties()
    ) {
        Card(
            modifier = modifier
                .width(388.dp)
                .height(433.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFFFFF),
            ),
            shape = RoundedCornerShape(30.dp),
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center). padding(7.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Reviews are great a way for us to " +
                            "improve our service or keep up the good work 😉." +
                            " We’d love to hear them!",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight(300),
                        color = Color(0xFF7B7B7B),
                        textAlign = TextAlign.Center,
                    )
                )

                Spacer(modifier = modifier.height(15.dp))

                Button(modifier = modifier
                    .width(323.09875.dp)
                    .height(54.67241.dp)
                    .background(
                        color = Color(0xFFF8BEB0),
                        shape = RoundedCornerShape(size = 20.dp)
                    ),
                    onClick = { navigateToReviewPage() }) {
                    Text("Add a Review")
                }

                TextButton(onClick = { onDismissRequest() }) {
                    Text("Maybe next time")
                }
            }
        }
    }
}
