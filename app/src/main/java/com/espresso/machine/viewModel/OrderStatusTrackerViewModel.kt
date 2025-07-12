package com.espresso.machine.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.espresso.machine.model.entity.Order
import com.espresso.machine.model.entity.OrderStatus
import com.espresso.machine.model.repository.OrderRepository
import kotlinx.coroutines.launch

class OrderStatusTrackerViewModel (private val orderRepository: OrderRepository) : ViewModel(){

    var state by mutableStateOf(OrderTrackerState())
        private set

    var order by mutableStateOf<Order?>(null)
        private set


    fun getOrderById(orderId: Int) = viewModelScope.launch {
        order = orderRepository.getOrderById(orderId)
    }



    fun onOrderPreparing(orderId: Int) = viewModelScope.launch{
        state = state.copy(status = OrderStatus.PREPARING)
        orderRepository.updateOrderStatus(orderId, state.status)
    }

    fun onOrderOnItsWay(orderId: Int)= viewModelScope.launch{
        state = state.copy(status = OrderStatus.ON_ITS_WAY)
        orderRepository.updateOrderStatus(orderId, state.status)
    }


    fun onOrderComplete(orderId: Int)= viewModelScope.launch{
        state = state.copy(status = OrderStatus.DONE)
        orderRepository.updateOrderStatus(orderId, state.status)
    }


}

class OrderStatusTrackerViewModelFactory(
    private val orderRepository: OrderRepository,
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(OrderStatusTrackerViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return OrderStatusTrackerViewModel(orderRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

data class OrderTrackerState(
    val status: OrderStatus = OrderStatus.AWAITING_CONFIRMATION,
)