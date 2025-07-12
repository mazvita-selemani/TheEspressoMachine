package com.espresso.machine.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.espresso.machine.model.entity.User
import com.espresso.machine.model.repository.UserRepository
import kotlinx.coroutines.launch

class RegisterViewModel (private val repository: UserRepository): ViewModel() {

    // state management,  this will observe the state of the input fields on the register form
    var state by mutableStateOf(RegisterState())
        private set

    fun onFirstNameChange(newValue: String){
        state = state.copy(firstName = newValue)
    }

    fun onLastNameChange(newValue: String){
        state = state.copy(lastName = newValue)
    }

    fun onEmailChange(newValue: String){
        state = state.copy(emailAddress = newValue)
    }

    fun onPasswordChange(newValue: String){
        state = state.copy(password = newValue)
    }

    fun onConfirmPasswordChange(newValue: String){
        state = state.copy(confirmPassword = newValue)
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(user: User) = viewModelScope.launch {
        repository.insert(user)
    }
}

class RegisterViewModelFactory(private val repository: UserRepository): ViewModelProvider.Factory{

    // checking that the viewmodel can be created
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RegisterViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

data class RegisterState(
    val firstName: String = "",
    val lastName: String = "",
    val emailAddress: String = "",
    val password: String = "",
    val confirmPassword: String = "",
)