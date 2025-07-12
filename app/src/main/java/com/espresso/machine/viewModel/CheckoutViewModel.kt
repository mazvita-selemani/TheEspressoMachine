package com.espresso.machine.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.espresso.machine.model.entity.Order
import com.espresso.machine.model.entity.OrderItem
import com.espresso.machine.model.entity.OrderStatus
import com.espresso.machine.model.repository.CardRepository
import com.espresso.machine.model.repository.OrderRepository
import com.espresso.machine.model.repository.UserRepository
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository,
    private val cardRepository: CardRepository,
    private val userEmailAddress: String
) : ViewModel() {

    var state by mutableStateOf(OrderState())

    val user by mutableStateOf(userRepository.findUserByEmail(userEmailAddress))

    var order by mutableStateOf<Order?>(null)
        private set

    val card by mutableStateOf(cardRepository.getCardByUserId(user.id))


    init {
        viewModelScope.launch {
            // Try to get the current open order for the user
            val currentOrder = orderRepository.getOpenOrderForUser(user.id)

            if (currentOrder != null) {
                order = currentOrder
                // If there's an open order, load its items
                loadOrderWithOrderItems(order!!.id)
                onTotalPriceChange()
            } else {
                // If no open order, create a new one
                order = Order(
                    userId = user.id,
                    isEmpty = true,
                    totalPrice = 0.0,
                    status = OrderStatus.IS_EMPTY
                )
                insert(order!!)
            }
        }
    }

    // this will only be called if order has 1 or more items
    private fun loadOrderWithOrderItems(orderId: Int) = viewModelScope.launch {
        val orderWithItems = orderRepository.getOrderWithOrderItems(orderId)
        state = state.copy(
            orderItems = orderWithItems.orderItems,
            totalPrice = orderWithItems.order.totalPrice ?: 0.0,
            isEmpty = orderWithItems.order.isEmpty ?: true,
            status = orderWithItems.order.status ?: OrderStatus.IS_EMPTY
        )
    }

    fun onOrderItemRemove(selected: OrderItem) {
        state = state.copy(orderItems = (state.orderItems - selected).toMutableList())
    }

    fun onOrderItemFocus(orderItem: OrderItem) {
        state = state.copy(orderItem = orderItem)
    }

    fun onTotalPriceChange() {
        state.orderItems.forEach { orderItem ->
            state = state.copy(totalPrice = state.totalPrice + orderItem.itemPriceTotal!!)
        }
    }

    fun getAvailableCard(): String? {
        return if (card != null){
            card.cardNumber!!.filter { it.isDigit() }.takeLast(4)
        } else {
            null
        }
    }

    fun increaseQuantity(orderItem: OrderItem) {
        val updatedItems = state.orderItems.map {
            if (it.id == orderItem.id) {
                val unitPrice = it.itemPriceTotal?.div(it.quantity!!)
                val newQuantity = (it.quantity ?: 0) + 1
                it.copy(quantity = newQuantity, itemPriceTotal = unitPrice?.times(newQuantity))
            } else it
        }.toMutableList()

        val newTotalPrice = updatedItems.sumOf { it.itemPriceTotal ?: 0.0 }

        state = state.copy(orderItems = updatedItems, totalPrice = newTotalPrice)
    }

    fun decreaseQuantity(orderItem: OrderItem) {
        val updatedItems = state.orderItems.map {
            if (it.id == orderItem.id) {
                val unitPrice = it.itemPriceTotal?.div(it.quantity!!)
                val newQuantity = (it.quantity ?: 0).coerceAtLeast(1) - 1
                it.copy(quantity = newQuantity, itemPriceTotal = unitPrice?.times(newQuantity))
            } else it
        }.toMutableList()

        val newTotalPrice = updatedItems.sumOf { it.itemPriceTotal ?: 0.0 }

        state = state.copy(orderItems = updatedItems, totalPrice = newTotalPrice)
    }


    fun insert(order: Order) = viewModelScope.launch {
        orderRepository.insert(order)
    }

    fun update() = viewModelScope.launch {

        onCheckoutConfirmed()

        // updating user current order
        order = order?.copy(
            isEmpty = false,
            totalPrice = state.totalPrice,
            status = state.status
        )


        orderRepository.update(order!!)

    }

    private fun onCheckoutConfirmed() {
        state = state.copy(status = OrderStatus.AWAITING_CONFIRMATION)
    }


}

class CheckoutViewModelFactory(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository,
    private val cardRepository: CardRepository,
    val userEmailAddress: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckoutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CheckoutViewModel(
                orderRepository,
                userRepository,
                cardRepository,
                userEmailAddress
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

data class OrderState(
    val orderItems: MutableList<OrderItem> = emptyList<OrderItem>().toMutableList(),
    val orderItem: OrderItem? = null,
    val totalPrice: Double = 0.0,
    val isEmpty: Boolean = true,
    val status: OrderStatus = OrderStatus.IS_EMPTY,
)

