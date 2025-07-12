package com.espresso.machine.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.espresso.machine.model.entity.User
import com.espresso.machine.model.repository.UserRepository

/**
 * Repository is completely separated from the UI through the ViewModel.
 */
class LoginViewModel(private val repository: UserRepository): ViewModel() {

    // state management,  this will observe the state of the input fields on the login form
    var state by mutableStateOf(LoginState())
        private set
    val isFieldsNotEmpty: Boolean
        get() = state.emailAddress.isEmpty() && state.password.isEmpty()


    fun onEmailChange(newValue: String){
        state = state.copy(emailAddress = newValue)
    }

    fun onPasswordChange(newValue: String){
        state = state.copy(password = newValue)
    }

    fun onIsActiveChange(newValue: Boolean){
        state = state.copy(isActive = newValue)
    }

    val allUsers: LiveData<List<User>> = repository.allUsers.asLiveData()


    fun getUserById(userId: Int): User {
        return repository.getUserById(userId)
    }

    /**
     * @see https://developer.android.com/topic/libraries/architecture/livedata#use_livedata_with_room
     * The main responsibility of the ViewModel is to load and manage UI-related data, which
     * makes it a great candidate for holding LiveData objects. Create LiveData objects
     * in the ViewModel and use them to expose state to the UI layer.
     */
    fun findUserByEmail(): User{
        return repository.findUserByEmail(state.emailAddress)
    }


}

class LoginViewModelFactory(private val repository: UserRepository): ViewModelProvider.Factory{

    // checking that the viewmodel can be created
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LoginViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

data class LoginState(
    val emailAddress: String = "",
    val password: String = "",
    val isActive: Boolean = false,
)