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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.espresso.machine.components.HeaderText
import com.espresso.machine.components.LoginTextField
import com.espresso.machine.ui.theme.TheEspressoMachineTheme

private val defaultPadding = 16.dp
private val itemSpacing = 8.dp

@Composable
fun ForgotPassword() {
    // state managers
    val (password, setPassword) = rememberSaveable {
        mutableStateOf("")
    }

    val (confirmPassword, setConfirmPassword) = rememberSaveable {
        mutableStateOf("")
    }

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
            value = password,
            onValueChange = setPassword,
            labelText = "Password",
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Password,
            // masking composable
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(itemSpacing))

        LoginTextField(
            value = confirmPassword,
            onValueChange = setConfirmPassword,
            labelText = "Confirm Password",
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Password,
            // masking composable
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(itemSpacing))

        // buttons
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Password")
        }

        Spacer(Modifier.height(itemSpacing))

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel")
        }


    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewForgotPasswordScreen() {
    TheEspressoMachineTheme {
        ForgotPassword()
    }
}