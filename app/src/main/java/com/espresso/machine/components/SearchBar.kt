package com.espresso.machine.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
){
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        placeholder = {Text("Search")}
    )
}

/*
@Preview ( showBackground = true)
@Composable
fun PreviewSearchBar(){
    SearchBar(
        Modifier
            .border(width = 0.dp, color = Color(0xFFC3BDBD), shape = RoundedCornerShape(size = 30.dp))
        .background(color = Color(0xFFF4F4F4), shape = RoundedCornerShape(size = 30.dp))
        .padding(start = 30.dp, top = 10.dp, end = 30.dp, bottom = 10.dp)
    )
}*/
