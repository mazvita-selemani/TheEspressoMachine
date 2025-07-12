package com.espresso.machine

import com.espresso.machine.model.entity.User
import com.espresso.machine.model.repository.UserRepository
import com.espresso.machine.viewModel.LoginViewModel
import junit.framework.TestCase
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class LoginViewModelTest: TestCase() {


    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var loginViewModel: LoginViewModel

    fun setup(){
        MockitoAnnotations.openMocks(this)

        loginViewModel = LoginViewModel(userRepository)
    }

    fun testUser_is_returned_from_user_id(){

        val user = User(
            id = 20,
            firstName = "Mary",
            lastName = "Sandra",
            emailAddress = "ms@gmail.com",
            password = "User1234",
            availableCardId = null
        )

        `when`(userRepository.getUserById(user.id)).thenReturn(user)

        assertEquals(userRepository.getUserById(20), user)

    }


}