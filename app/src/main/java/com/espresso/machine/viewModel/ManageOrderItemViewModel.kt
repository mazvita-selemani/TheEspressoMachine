package com.espresso.machine.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.espresso.machine.model.entity.Food
import com.espresso.machine.model.entity.MilkType
import com.espresso.machine.model.entity.OrderItem
import com.espresso.machine.model.entity.Size
import com.espresso.machine.model.repository.FoodRepository
import com.espresso.machine.model.repository.OrderItemRepository
import kotlinx.coroutines.launch

class ManageOrderItemViewModel(
    private val orderItemRepository: OrderItemRepository,
    private val foodRepository: FoodRepository,
    private val foodItemId: Int? = null,
    val updatedOrderItemId: Int? = null,
) : ViewModel() {

    var state by mutableStateOf(OrderItemState())
        private set

    var food by mutableStateOf<Food?>(null)
        private set

    var updatedOrderItem by mutableStateOf<OrderItem?>(null)
        private set

    init {
        if (updatedOrderItemId == null && foodItemId != null) {
            loadFoodItemDetailsById(foodItemId)
            viewModelScope.launch {
                food =
                    foodRepository.getFoodItemById(foodItemId) // TODO get it to be in the other thread instead of main in database builder Fetch asynchronously
                state = state.copy(foodName = food?.name!!)
                state = state.copy(foodResId = food?.resId!!)
            }
        } else {
            viewModelScope.launch {
                updatedOrderItem = orderItemRepository.getOrderItemById(updatedOrderItemId!!)
                updatedOrderItem?.let {
                    state = state.copy(
                        foodItemId = it.foodItemId!!,
                        foodName = it.foodName!!,
                        foodResId = it.foodResId!!,
                        isUpdated = true,
                        size = it.size!!,
                        itemPriceTotal = it.itemPriceTotal!!,
                        quantity = it.quantity!!
                    )
                }
            }
        }


    }

    private fun loadFoodItemDetailsById(foodItemId: Int) {
        state = state.copy(foodItemId = foodItemId)

    }


    fun onItemPriceTotalChange() {
        val itemPriceTotal = food?.price?.times(state.quantity)
        state = state.copy(itemPriceTotal = itemPriceTotal)
    }

    fun onSizeChange(newValue: String) {
        state = state.copy(size = Size.valueOf(newValue))
    }

    fun onMilkTypeChange(newValue: String) {
        state = state.copy(milkType = MilkType.valueOf(newValue))
    }

    fun addOrderItem(orderItem: OrderItem) = viewModelScope.launch {
        state = state.copy(isUpdated = true)
        orderItemRepository.insert(orderItem)
    }

    fun increaseQuantity() {
        state = state.copy(quantity = state.quantity + 1)
    }

    fun decreaseQuantity() {
        state = state.copy(quantity = state.quantity - 1)
    }

    fun updateOrderItem(orderItem: OrderItem) = viewModelScope.launch {
        orderItemRepository.update(orderItem)
    }

    fun deleteOrderItem(orderItem: OrderItem) = viewModelScope.launch {
        orderItemRepository.delete(orderItem)
    }

    fun getOrderItemById(orderItemId: Int): OrderItem {
        return orderItemRepository.getOrderItemById(orderItemId)
    }

}

class ManageOrderItemViewModelFactory(
    private val orderItemRepository: OrderItemRepository,
    private val foodRepository: FoodRepository,
    private val foodItemId: Int? = null,
    private val updatedOrderItemId: Int? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManageOrderItemViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ManageOrderItemViewModel(
                orderItemRepository,
                foodRepository,
                foodItemId,
                updatedOrderItemId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

data class OrderItemState(
    val foodItemId: Int = 0, //will get passed down from main menu
    val foodName: String = "", //will get passed down from main menu
    val foodResId: Int = 0, //will get passed down from main menu
    val isUpdated: Boolean = false,
    val size: Size? = null,
    val milkType: MilkType? = null,
    val itemPriceTotal: Double? = 0.0, // initialised as food.price * quantity
    val quantity: Int = 0,
)