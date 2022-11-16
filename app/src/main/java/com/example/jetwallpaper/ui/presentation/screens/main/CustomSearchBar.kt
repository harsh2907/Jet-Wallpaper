package com.example.jetwallpaper.ui.presentation.screens.main

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetwallpaper.ui.presentation.viewmodel.MainViewModel
import com.example.jetwallpaper.ui.theme.Violet


@Composable
fun CustomSearchBar(
    modifier: Modifier = Modifier,
    hint: String = "Search..",
    viewModel:MainViewModel,
    onSearch:(String)->Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    TextField(
        value = searchQuery,
        onValueChange = {
            searchQuery = it
        },
        placeholder = {
            Text(text = hint, color = Color.Gray, fontSize = 24.sp)
        },
        keyboardActions = KeyboardActions(onSearch = {
            onSearch(searchQuery)
        }),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Violet,
            cursorColor = Violet,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .onFocusChanged {
//                if (!it.isFocused && searchQuery == "") {
//                    viewModel.loadInitialWallpapers()
//                }
            }
    )

}