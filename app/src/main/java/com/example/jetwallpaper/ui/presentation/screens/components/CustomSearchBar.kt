package com.example.jetwallpaper.ui.presentation.screens.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.jetwallpaper.ui.theme.UiColors


@Composable
fun CustomSearchBar(
    modifier: Modifier = Modifier,
    hint: String = "Search..",
    onSearch: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var searchQuery by remember { mutableStateOf("") }
    val containerColor = if(isSystemInDarkTheme()) UiColors.BottomNavColor else Color.White
    val elevation = if(!isSystemInDarkTheme()) 12.dp else 0.dp


    TextField(
        value = searchQuery,
        onValueChange = {
            searchQuery = it
        },
        placeholder = {
            Text(text = hint, color = Color.Gray)
        },
        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus()

                if(searchQuery.isNotEmpty()){
                    onSearch(searchQuery)
                }
            }
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        colors = TextFieldDefaults.colors(
            cursorColor = UiColors.Violet,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            focusedTextColor = MaterialTheme.colorScheme.onBackground
        ),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(
                elevation = elevation,
                shape = MaterialTheme.shapes.medium
            )
            .then(modifier),
        shape = CircleShape
    )

}
