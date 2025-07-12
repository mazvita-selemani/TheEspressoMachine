package com.espresso.machine

import com.espresso.machine.model.entity.Card
import com.espresso.machine.model.entity.MilkType
import com.espresso.machine.model.entity.Order
import com.espresso.machine.model.entity.OrderItem
import com.espresso.machine.model.entity.OrderStatus
import com.espresso.machine.model.entity.Size
import com.espresso.machine.model.entity.User
import com.espresso.machine.model.repository.CardRepository
import com.espresso.machine.model.repository.OrderRepository
import com.espresso.machine.model.repository.UserRepository
import com.espresso.machine.viewModel.CheckoutViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CheckoutViewModelTest {

    /*@get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()*/


    // Mock repositories
    @Mock
    private var orderRepository: OrderRepository = mock(OrderRepository::class.java)

    @Mock
    private var userRepository: UserRepository = mock(UserRepository::class.java)

    @Mock
    private var cardRepository: CardRepository = mock(CardRepository::class.java)

    // ViewModel instance
    private lateinit var viewModel: CheckoutViewModel


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

    private val mockUser = User(
        firstName = "Mary",
        lastName = "Sandra",
        emailAddress = "ms@gmail.com",
        password = "User1234",
        availableCardId = null
    )
    private val mockCard = Card(
        cardNumber = "1234567890123456",
        userId = 1,
        cardHolderName = null,
        expiryDate = "09/28",
        cvc = "1234"
    )
    private val mockOrder = Order(
        id = 1, userId = mockUser.id, isEmpty = true, totalPrice = 0.0,
        status = OrderStatus.IS_EMPTY
    )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)

        // stubbing mock objects
        `when`(userRepository.findUserByEmail(mockUser.emailAddress!!)).thenReturn(mockUser)
        `when`(cardRepository.getCardByUserId(mockUser.id)).thenReturn(mockCard)
        `when`(orderRepository.getOpenOrderForUser(mockUser.id)).thenReturn(mockOrder)
      //  `when`(orderRepository.getOrderWithOrderItems())

        // Initialize ViewModel with mocked dependencies
        viewModel = CheckoutViewModel(orderRepository, userRepository, cardRepository,
            mockUser.emailAddress!!
        )


        viewModel.state = viewModel.state.copy(orderItems = mutableListOf(orderItem))
    }

    @Test
    fun `initialization loads open order if available`()  {
        // Verify the ViewModel loads an existing order if available
        verify(orderRepository).getOpenOrderForUser(mockUser.id)
        assertEquals(mockOrder, viewModel.order)
    }

    @Test
    fun `increaseQuantity updates order item quantity and total price`()  {

        // Act
        viewModel.increaseQuantity(orderItem)

        // Assert
        val updatedOrderItem = viewModel.state.orderItems.first()
        assertEquals(2, updatedOrderItem.quantity)
        assertEquals(20.0, updatedOrderItem.itemPriceTotal!!, 0.001)
        assertEquals(20.0, viewModel.state.totalPrice, 0.001)
    }

    @Test
    fun `decreaseQuantity updates order item quantity and total price`()  {

        // Act
        viewModel.decreaseQuantity(orderItem)

        // Assert
        val updatedOrderItem = viewModel.state.orderItems.first()
        assertEquals(1, updatedOrderItem.quantity)
        assertEquals(10.0, updatedOrderItem.itemPriceTotal!!, 0.001)
        assertEquals(10.0, viewModel.state.totalPrice, 0.001)
    }

    @Test
    fun `getAvailableCard returns last 4 digits of card number`() {
        // Act
        val lastFourDigits = viewModel.getAvailableCard()

        // Assert
        assertEquals("3456", lastFourDigits)
    }
}
