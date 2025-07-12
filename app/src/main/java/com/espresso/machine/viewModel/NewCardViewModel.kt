package com.espresso.machine.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.espresso.machine.model.entity.Card
import com.espresso.machine.model.entity.User
import com.espresso.machine.model.repository.CardRepository
import com.espresso.machine.model.repository.UserRepository
import kotlinx.coroutines.launch

class NewCardViewModel(private val repository: CardRepository, private val userRepository: UserRepository): ViewModel() {
    var state by mutableStateOf(NewCardState())
        private set

    fun onCardHolderNameChange(newValue: String){
        state = state.copy(cardHolderName = newValue)
    }

    fun onCardNumberChange(newValue: String){
        state = state.copy(cardNumber = newValue)
    }

    fun onExpiryDateChange(newValue: String){
        state = state.copy(expiryDate = newValue)
    }

    fun onCvcChange(newValue: String){
        state = state.copy(cvc = newValue)
    }

    fun findUserByEmail(email: String): User{
        return userRepository.findUserByEmail(email)
    }

    fun findCardByCvc(cvc: Int): Card{
        return repository.findCardByCvc(cvc)
    }

    fun getCardByUserId(userId: Int): Card{
        return repository.getCardByUserId(userId)
    }

    fun insert(card: Card) = viewModelScope.launch {
        repository.insert(card)
    }

    fun delete(card: Card) = viewModelScope.launch {
        repository.delete(card)
    }
}

class NewCardViewModelFactory(private val repository: CardRepository, private val userRepository: UserRepository): ViewModelProvider.Factory{

    // checking that the viewmodel can be created
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NewCardViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return NewCardViewModel(repository, userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

data class NewCardState(
    val cardHolderName: String = "",
    val cardNumber: String = "",
    val expiryDate: String = "",
    val cvc: String = ""
)