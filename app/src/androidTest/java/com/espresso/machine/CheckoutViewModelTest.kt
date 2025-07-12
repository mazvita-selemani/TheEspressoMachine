package com.espresso.machine

import com.espresso.machine.model.entity.Card
import com.espresso.machine.model.entity.MilkType
import com.espresso.machine.model.entity.Order
import com.espresso.machine.model.entity.OrderItem
import com.espresso.machine.model.entity.OrderStatus
import com.espresso.machine.model.entity.OrderWithOrderItems
import com.espresso.machine.model.entity.Size
import com.espresso.machine.model.entity.User
import com.espresso.machine.model.repository.CardRepository
import com.espresso.machine.model.repository.OrderRepository
import com.espresso.machine.model.repository.UserRepository
import com.espresso.machine.viewModel.CheckoutViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CheckoutViewModelTest {

    // Mock dependencies
    @Mock
    private lateinit var orderRepository: OrderRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var cardRepository: CardRepository

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: CheckoutViewModel

    // Sample data
    private val orderItem = OrderItem(
        quantity = 1,
        itemPriceTotal = 10.0,
        orderId = 50,
        foodItemId = 100,
        foodName = "Latte",
        foodResId = null,
        isUpdated = false,
        size = Size.SMALL,
        milkType = MilkType.OAT
    )

    private val user = User(
        firstName = "Mary",
        lastName = "Sandra",
        emailAddress = "ms@gmail.com",
        password = "User1234",
        availableCardId = null
    )
    private val card = Card(
        cardNumber = "1234567890123456",
        userId = 1,
        cardHolderName = null,
        expiryDate = "09/28",
        cvc = "1234"
    )
    private val order = Order(
        id = 1, userId = user.id, isEmpty = true, totalPrice = 0.0,
        status = OrderStatus.IS_EMPTY
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // Define Mockito behavior
        `when`(userRepository.findUserByEmail(user.emailAddress!!)).thenReturn(user)
        `when`(cardRepository.getCardByUserId(user.id)).thenReturn(card)
        `when`(orderRepository.getOpenOrderForUser(user.id)).thenReturn(null)

        // Initialize ViewModel with mocked dependencies
        viewModel = CheckoutViewModel(
            orderRepository = orderRepository,
            userRepository = userRepository,
            cardRepository = cardRepository,
            userEmailAddress = user.emailAddress!!
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initial_state_is_set_up_correctly() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        // Check initial state
        assertEquals(user, viewModel.user)
        assertEquals(card, viewModel.card)
        assertEquals(OrderStatus.IS_EMPTY, viewModel.state.status)
    }

    @Test
    fun insert_new_order_when_no_open_order_exists() = runTest {
        // Act
        viewModel = CheckoutViewModel(
            orderRepository = orderRepository,
            userRepository = userRepository,
            cardRepository = cardRepository,
            userEmailAddress = user.emailAddress!!
        )
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify that insert was called
        verify(orderRepository, times(1)).insert(any())
    }

    @Test
    fun load_existing_order_when_open_order_exist() = runTest {
        // Arrange
        `when`(orderRepository.getOpenOrderForUser(user.id)).thenReturn(order)
        `when`(orderRepository.getOrderWithOrderItems(order.id)).thenReturn(
            OrderWithOrderItems(order = order, orderItems = mutableListOf(orderItem))
        )

        // Act
        viewModel = CheckoutViewModel(
            orderRepository = orderRepository,
            userRepository = userRepository,
            cardRepository = cardRepository,
            userEmailAddress = user.emailAddress!!
        )
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(mutableListOf(orderItem), viewModel.state.orderItems)
        assertEquals(OrderStatus.IS_EMPTY, viewModel.state.status)
    }


    @Test
    fun onCheckoutConfirmed_sets_status_to_AWAITING_CONFIRMATION() = runTest {
        viewModel.update()
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify the updated state
        assertEquals(OrderStatus.AWAITING_CONFIRMATION, viewModel.state.status)
    }
}
