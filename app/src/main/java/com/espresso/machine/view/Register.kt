package com.espresso.machine.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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
import com.espresso.machine.viewModel.RegisterState
import com.espresso.machine.viewModel.RegisterViewModel
import com.espresso.machine.viewModel.RegisterViewModelFactory

private val defaultPadding = 16.dp
private val itemSpacing = 8.dp

@Composable
fun RegisterScreen(
    repository: UserRepository,
    navigateUp: () -> Unit
) {
    val viewModel = viewModel<RegisterViewModel>(factory = RegisterViewModelFactory(repository))
    val user = User(
        firstName = viewModel.state.firstName,
        lastName = viewModel.state.lastName,
        emailAddress = viewModel.state.emailAddress,
        password = viewModel.state.password,
        availableCardId = 0
    )

    Register(
        navigateUp = { navigateUp() },
        state = viewModel.state,
        onFirstNameChange = viewModel::onFirstNameChange,
        onLastNameChange = viewModel::onLastNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        insert = { viewModel.insert(user) }
    )
}

@Composable
private fun Register(

    navigateUp: () -> Unit,
    state: RegisterState,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    insert: () -> Unit

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
            text = "Sign Up",
            modifier = Modifier
                .padding(vertical = defaultPadding)
        )
        LoginTextField(
            value = state.firstName,
            onValueChange = { onFirstNameChange(it) },
            labelText = "First name",
            modifier = Modifier.fillMaxWidth()
        )

        //space between items
        Spacer(Modifier.height(itemSpacing))

        LoginTextField(
            value = state.lastName,
            onValueChange = { onLastNameChange(it) },
            labelText = "Last name",
            modifier = Modifier.fillMaxWidth()
        )

        //space between items
        Spacer(Modifier.height(itemSpacing))

        LoginTextField(
            value = state.emailAddress,
            onValueChange = { onEmailChange(it) },
            labelText = "Email address",
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

        Spacer(Modifier.height(itemSpacing))

        // buttons
        Button(
            onClick = {
                insert.invoke()
                navigateUp()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = state.firstName.isNotEmpty() &&
                    state.lastName.isNotEmpty() &&
                    state.emailAddress.isNotEmpty() &&
                    state.password.isNotEmpty()
        ) {
            Text("Save")
        }

        Spacer(Modifier.height(itemSpacing))

        Button(
            onClick = { navigateUp() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel")
        }


    }
}

/*
@Preview(showSystemUi = true)
@Composable
fun PreviewRegisterScreen() {
    TheEspressoMachineTheme {
        Register()
    }
}*/
