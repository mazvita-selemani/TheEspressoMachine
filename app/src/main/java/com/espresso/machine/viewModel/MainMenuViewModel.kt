package com.espresso.machine.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.espresso.machine.R
import com.espresso.machine.model.entity.Category
import com.espresso.machine.model.entity.Food
import com.espresso.machine.model.entity.Order
import com.espresso.machine.model.entity.User
import com.espresso.machine.model.repository.FoodRepository
import com.espresso.machine.model.repository.OrderRepository
import com.espresso.machine.model.repository.UserRepository
import kotlinx.coroutines.launch


class MainMenuViewModel(
    private val repository: FoodRepository,
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    var state by mutableStateOf(MainMenuState())
        private set

    init {
        loadItems()
        /*deleteAll()
        insertAll( *foodItemsDataProvider.toTypedArray())*/
    }

    private fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }

    private fun insertAll(vararg foods: Food) = viewModelScope.launch {
        repository.insertAll(*foods)
    }

    fun getOrderById(orderId : Int): Order {
        return orderRepository.getOrderById(orderId)
    }

    fun loadItems(): LiveData<List<Food>> {
        return repository.allFoods.asLiveData()
    }

    fun findUserByEmail(emailAddress: String): User {
        return userRepository.findUserByEmail(emailAddress)
    }

    fun onSearchChange(newValue: String) {
        state = state.copy(search = newValue)
    }

    fun onCategoryChange(newValue: String) {
        state = state.copy(category = Category.valueOf(newValue))
    }

    fun loadItemsByCategory(category: String): LiveData<List<Food>> {
        return repository.getAllByCategory(category).asLiveData()
    }

}

class MainMenuViewModelFactory(
    private val repository: FoodRepository,
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository
) : ViewModelProvider.Factory {

    // checking that the viewmodel can be created
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainMenuViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainMenuViewModel(repository, userRepository, orderRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

val foodItemsDataProvider = listOf(
    Food(
        category = Category.HOT_DRINK,
        name = "Cappuccino",
        price = 2.50,
        resId = R.drawable.latte
    ),
    Food(
        category = Category.HOT_DRINK,
        name = "Espresso",
        price = 1.50,
        resId = R.drawable.hot_drink
    ),
    Food(
        category = Category.HOT_DRINK,
        name = "Matcha",
        price = 3.50,
        resId = R.drawable.hot_matcha
    ),
    Food(
        category = Category.HOT_DRINK,
        name = "Hot Chocolate",
        price = 1.50,
        resId = R.drawable.hot_chocolate
    ),
    Food(
        category = Category.COLD_DRINK,
        name = "Iced Latte",
        price = 2.00,
        resId = R.drawable.iced_latte
    ),
    Food(
        category = Category.COLD_DRINK,
        name = "Iced Tea",
        price = 2.50,
        resId = R.drawable.cold_ice_tea
    ),
    Food(
        category = Category.COLD_DRINK,
        name = "Cinnamon Latte",
        price = 1.50,
        resId = R.drawable.cold_cinnamon_latte
    ),
    Food(
        category = Category.COLD_DRINK,
        name = "Milkshake",
        price = 1.50,
        resId = R.drawable.cold_milkshake
    ),
    Food(
        category = Category.BAKED_GOODS,
        name = "Croissant",
        price = 2.50,
        resId = R.drawable.baked_croissant
    ),
    Food(
        category = Category.BAKED_GOODS,
        name = "Cinnamon Roll",
        price = 2.00,
        resId = R.drawable.baked_cinnamon_roll
    ),
    Food(
        category = Category.BAKED_GOODS,
        name = "Chocolate Cake",
        price = 2.50,
        resId = R.drawable.baked_chocolate_cake
    ),
    Food(
        category = Category.BAKED_GOODS,
        name = "Sponge Cake",
        price = 2.50,
        resId = R.drawable.baked_sponge_cake
    ),
)


data class MainMenuState(
    val foodItems: List<Food> = emptyList(),
    val category: Category = Category.HOT_DRINK, // when main menu is opened this should be the default
    val search: String = ""
)