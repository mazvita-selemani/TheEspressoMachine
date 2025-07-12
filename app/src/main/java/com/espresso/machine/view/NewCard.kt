package com.espresso.machine.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.espresso.machine.components.HeaderText
import com.espresso.machine.model.entity.Card
import com.espresso.machine.model.repository.CardRepository
import com.espresso.machine.model.repository.UserRepository
import com.espresso.machine.viewModel.NewCardState
import com.espresso.machine.viewModel.NewCardViewModel
import com.espresso.machine.viewModel.NewCardViewModelFactory
import java.util.Calendar

private val defaultPadding = 16.dp
private val itemSpacing = 8.dp

@Composable
fun NewCardScreen(
    repository: CardRepository,
    userRepository: UserRepository,
    navigateUp: () -> Unit,
    userEmail: String
) {

    val viewModel = viewModel<NewCardViewModel>(factory = NewCardViewModelFactory(repository, userRepository))
    val user by lazy { viewModel.findUserByEmail(userEmail) }

    val card =
        Card(
            cardHolderName = viewModel.state.cardHolderName,
            cardNumber = viewModel.state.cardNumber,
            expiryDate = viewModel.state.expiryDate,
            cvc = viewModel.state.cvc,
            userId = user.id
        )


    NewCard(
        navigateUp = { navigateUp() },
        state = viewModel.state,
        onCardHolderNameChange = viewModel::onCardHolderNameChange,
        onCardNumberChange = viewModel::onCardNumberChange,
        onExpiryDateChange = viewModel::onExpiryDateChange,
        onCvcChange = viewModel::onCvcChange,
        insert = {
            viewModel.insert(card)
        },
    )
}

@Composable
private fun NewCard(
    navigateUp: () -> Unit,
    state: NewCardState,
    onCardHolderNameChange: (String) -> Unit,
    onCardNumberChange: (String) -> Unit,
    onExpiryDateChange: (String) -> Unit,
    onCvcChange: (String) -> Unit,
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
            text = "Add a New Card",
            modifier = Modifier
                .padding(vertical = defaultPadding)
        )

        // Card Number Field
        OutlinedTextField(
            value = state.cardNumber,
            onValueChange = { it ->
                if (it.length <= 19) { // Max length for formatted Visa card is 19 (16 digits + 3 spaces)
                    val formatted = it.filter { it.isDigit() }
                        .chunked(4)
                        .joinToString(" ")
                    onCardNumberChange(formatted)
                }
            },
            label = { Text("Card Number") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            isError = state.cardNumber.replace(" ", "").length != 16, // Visa requires 16 digits
            singleLine = true,
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Expiry Date Field (MM/YY)
        OutlinedTextField(
            value = state.expiryDate,
            onValueChange = {
                if (it.length <= 5) { // Limit length to 5 (MM/YY)
                    val formatted = it.filter { it.isDigit() }
                        .chunked(2)
                        .joinToString("/")
                        .take(5) // Limit to "MM/YY"
                    onExpiryDateChange(formatted)
                }
            },
            label = { Text("Expiry Date (MM/YY)") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            isError = !isValidExpiryDate(state.expiryDate),
            singleLine = true,
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // CVC Field
        OutlinedTextField(
            value = state.cvc,
            onValueChange = {
                if (it.length <= 4 && it.all { char -> char.isDigit() }) {
                    onCvcChange(it)
                }
            },
            label = { Text("CVC") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            isError = state.cvc.length !in 3..4, // CVC should be 3-4 digits
            singleLine = true,
            shape = RoundedCornerShape(8.dp)
        )


        //space between items
        Spacer(Modifier.height(itemSpacing))

        // buttons
        Button(
            onClick = {
                insert.invoke()
                navigateUp()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Card")
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

// Helper function to validate expiry date (simple MM/YY format)
private fun isValidExpiryDate(expiryDate: String): Boolean {
    val dateParts = expiryDate.split("/")
    if (dateParts.size != 2) return false
    val (monthStr, yearStr) = dateParts
    val month = monthStr.toIntOrNull()
    val year = yearStr.toIntOrNull()
    if (month == null || year == null) return false
    if (month !in 1..12) return false

    val currentYear = Calendar.getInstance().get(Calendar.YEAR) % 100
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1

    return (year > currentYear || (year == currentYear && month >= currentMonth))
}


/*
@Preview(showSystemUi = true)
@Composable
fun PreviewNewCardScreen() {
    TheEspressoMachineTheme {
        NewCard()
    }
}*/
