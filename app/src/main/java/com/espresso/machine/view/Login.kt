package com.espresso.machine.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.espresso.machine.components.HeaderText
import com.espresso.machine.components.LoginTextField
import com.espresso.machine.model.entity.User
import com.espresso.machine.model.repository.UserRepository
import com.espresso.machine.viewModel.LoginState
import com.espresso.machine.viewModel.LoginViewModel
import com.espresso.machine.viewModel.LoginViewModelFactory

private val defaultPadding = 16.dp
private val itemSpacing = 8.dp

// in jetpack compose everything is stateless so you have to create your STATE

@Composable
fun LoginScreen(
    repository: UserRepository,
    onLogin: (User) -> Unit,
    onRegister: () -> Unit
){
    val viewModel = viewModel<LoginViewModel>(factory = LoginViewModelFactory(repository))
    val user by lazy { viewModel.findUserByEmail() }

    Login(
        onSuccessfulLogin = { onLogin(user) },
        onRegister = {onRegister()},
        state = viewModel.state,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onRememberMeChecked = viewModel::onIsActiveChange
    )
}

@Composable
private fun Login(
    onSuccessfulLogin: () -> Unit,
    onRegister: () -> Unit,
    state: LoginState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberMeChecked: (Boolean) -> Unit,
) {

    Column(
        // a column of components with horizontal padding
        modifier = Modifier
            .fillMaxSize()
            .padding(defaultPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HeaderText(
            text = "Login",
            modifier = Modifier
                .padding(vertical = defaultPadding)
        )
        LoginTextField(
            value = state.emailAddress,
            onValueChange = { onEmailChange(it) },
            labelText = "Username",
            modifier = Modifier.fillMaxWidth()
        )

        //space between items
        Spacer(Modifier.height(itemSpacing))

        LoginTextField(
            value = state.password,
            onValueChange = { onPasswordChange(it) },
            labelText = "Password",
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Password,
            // masking composable
            visualTransformation = PasswordVisualTransformation()
        )

        //space between items
        Spacer(Modifier.height(itemSpacing))

        // remember me and forgot password
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = state.isActive,
                    onCheckedChange = {onRememberMeChecked(it)}
                )
                Text("Remember me")
            }
            TextButton(onClick = {}) {
                Text("Forgot Password?")
            }
        }

        Spacer(Modifier.height(itemSpacing))

        // buttons
        Button(
            onClick = {onSuccessfulLogin()},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign In")
        }

        Spacer(Modifier.height(itemSpacing))

        Button(
            onClick = {onRegister()},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up")
        }


    }
}

sealed class LoginEvent {
    data class SuccessfulLogin(val username: String, val password: String) : LoginEvent()
    data object Register : LoginEvent()
    data class ForgotPassword(val username: String) : LoginEvent()
}


/*
@Preview(showSystemUi = true)
@Composable
fun PreviewLoginScreen() {
    TheEspressoMachineTheme {
        Login()
    }
}*/
