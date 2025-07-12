package com.espresso.machine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.espresso.machine.model.AppDatabase
import com.espresso.machine.model.repository.CardRepository
import com.espresso.machine.model.repository.FoodRepository
import com.espresso.machine.model.repository.OrderItemRepository
import com.espresso.machine.model.repository.OrderRepository
import com.espresso.machine.model.repository.UserRepository
import com.espresso.machine.ui.theme.TheEspressoMachineTheme
import com.espresso.machine.view.CheckoutScreen
import com.espresso.machine.view.ForgotPassword
import com.espresso.machine.view.GetStarted
import com.espresso.machine.view.LoginScreen
import com.espresso.machine.view.MainMenuScreen
import com.espresso.machine.view.ManageOrderItemScreen
import com.espresso.machine.view.NewCardScreen
import com.espresso.machine.view.OrderStatusTrackerScreen
import com.espresso.machine.view.RegisterScreen
import com.espresso.machine.view.ReviewScreen
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {

    @Serializable
    object GetStartedRoute

    @Serializable
    object Login

    @Serializable
    object Register

    @Serializable
    data class Review(val username: String)

    @Serializable
    data class MainMenu(val username: String, val orderId: Int? = null)

    @Serializable
    data class ManageOrder(
        val username: String,
        val orderId: Int? = null,
        val foodId: Int? = null,
        val updateOrderItemId: Int? = null
    )

    @Serializable
    data class Checkout(val username: String)

    @Serializable
    data class Card(val username: String)

    @Serializable
    data class OrderStatusTracker(val username: String, val orderId: Int)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheEspressoMachineTheme {

                val database by lazy { AppDatabase.getDatabase(this) }
                val repository by lazy { UserRepository(database.userDao()) }
                val foodRepository by lazy { FoodRepository(database.foodDao()) }
                val cardRepository by lazy { CardRepository(database.cardDao()) }
                val orderItemRepository by lazy { OrderItemRepository(database.orderItemDao()) }
                val orderRepository by lazy { OrderRepository(database.orderDao()) }


                val navController = rememberNavController()

                NavHost(navController, startDestination = GetStartedRoute) {


                    composable<GetStartedRoute> {
                        GetStarted {
                            navController.navigate(Login)
                        }
                    }


                    composable<Login> {
                        LoginScreen(
                            repository = repository,
                            onRegister = { navController.navigate(Register) },
                            onLogin = { user ->
                                val email = user.emailAddress
                                navController.navigate(MainMenu(username = email!!))
                            },
                        )
                    }


                    composable<Register> {
                        RegisterScreen(repository) {
                            navController.navigate(Login)
                        }
                    }


                    composable("forgot_password/{username}") { backStackEntry ->
                        val username = backStackEntry.arguments?.getString("username") ?: ""
                        ForgotPassword()
                    }


                    composable<MainMenu>(
                        exitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Down,
                                tween(800)
                            )
                        },
                        popEnterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Down,
                                tween(800)
                            )
                        },
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Down,
                                tween(800)
                            )
                        }
                    ) { backStackEntry ->
                        val mainMenu: MainMenu = backStackEntry.toRoute()
                        val orderId = mainMenu.orderId
                        val username = mainMenu.username

                        MainMenuScreen(
                            userName = username,
                            orderId = orderId,
                            foodRepository = foodRepository,
                            userRepository = repository,
                            orderRepository = orderRepository,
                            onItemClick = { id ->
                                navController.navigate(
                                    ManageOrder(
                                        username = username,
                                        orderId = orderId,
                                        foodId = id
                                    )
                                )
                            },
                            navigateToCheckout = { navController.navigate(Checkout(username = username)) },
                            navigateToReviewPage = {navController.navigate(Review(username))}
                        )
                    }


                    composable<ManageOrder>(
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Down,
                                tween(800)
                            )
                        },
                    ) { backStackEntry ->

                        val manageOrder: ManageOrder = backStackEntry.toRoute()
                        val username = manageOrder.username
                        val foodId = manageOrder.foodId
                        val orderId = manageOrder.orderId
                        val updateOrderItemId = manageOrder.updateOrderItemId

                        ManageOrderItemScreen(
                            orderItemRepository = orderItemRepository,
                            foodRepository = foodRepository,
                            loadFoodItemDetails = foodId,
                            orderId = orderId!!,
                            updateOrderItemId = updateOrderItemId,
                            navigateUp = {
                                navController.navigate(
                                    MainMenu(
                                        username = username,
                                        orderId = orderId
                                    )
                                )
                            }
                        )
                    }


                    composable<Checkout>(
                        popEnterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Down,
                                tween(800)
                            )
                        },
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Down,
                                tween(800)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Down,
                                tween(800)
                            )
                        },
                    ) { backStackEntry ->

                        val checkout: Checkout = backStackEntry.toRoute()
                        val username = checkout.username
                        CheckoutScreen(
                            orderRepository = orderRepository,
                            userRepository = repository,
                            cardRepository = cardRepository,
                            userEmailAddress = username,
                            navigateToHome = { order ->
                                val orderId = order.id
                                navController.navigate(
                                    MainMenu(
                                        username = username,
                                        orderId = orderId
                                    )
                                )
                            },
                            onOrderItemUpdate = { orderItem, order ->
                                val orderItemId = orderItem.id
                                val orderId = order.id
                                navController.navigate(
                                    ManageOrder(
                                        username = username,
                                        updateOrderItemId = orderItemId,
                                        orderId = orderId
                                    )
                                )
                            },
                            navigateToAddCard = {
                                navController.navigate(Card(username = username))
                            },
                            navigateToOrderStatusTracker = { order ->
                                val orderId = order.id
                                navController.navigate(
                                    OrderStatusTracker(
                                        username = username,
                                        orderId = orderId
                                    )
                                )
                            }
                        )
                    }


                    composable<Card> { backStackEntry ->
                        val addNewCard: Card = backStackEntry.toRoute()
                        val username = addNewCard.username

                        NewCardScreen(
                            repository = cardRepository,
                            userRepository = repository,
                            userEmail = username,
                            navigateUp = {
                                navController.navigate(Checkout(username = username))
                            }
                        )
                    }

                    composable<OrderStatusTracker> { backStackEntry ->
                        val orderStatusTracker: OrderStatusTracker = backStackEntry.toRoute()
                       val username = orderStatusTracker.username
                       val orderId = orderStatusTracker.orderId

                        OrderStatusTrackerScreen(
                            orderId = orderId,
                            orderRepository = orderRepository,
                            navigateToHome = {
                                navController.navigate(MainMenu(username, orderId))
                            }
                        )

                    }

                    composable<Review> { backStackEntry ->
                        val reviewScreen: Review = backStackEntry.toRoute()
                        val username = reviewScreen.username

                        ReviewScreen { navController.navigate(MainMenu(username = username)) } }

                }


            }
        }
    }
}
