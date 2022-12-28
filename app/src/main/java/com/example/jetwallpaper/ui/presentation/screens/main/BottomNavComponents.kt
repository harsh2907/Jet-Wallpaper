package com.example.jetwallpaper.ui.presentation.screens.main

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.jetwallpaper.ui.presentation.navigation.Screens
import com.example.jetwallpaper.ui.theme.*

sealed class BottomNavItem(val title:String, val icon:ImageVector, val route:String){

    object Popular: BottomNavItem("Search",Icons.Outlined.Search, Screens.Search.route)
    object New: BottomNavItem("New",Icons.Outlined.LocalFireDepartment, Screens.New.route)
    object Favourite: BottomNavItem("Favourite",Icons.Outlined.Favorite, Screens.Favourite.route)

}

val bottomNavItems = listOf(
    BottomNavItem.Popular,
    BottomNavItem.New,
    BottomNavItem.Favourite
)

@Composable
fun BottomNav(navController: NavHostController) {

    BottomNavigation(
        modifier = Modifier
            .graphicsLayer{
                shape = RoundedCornerShape(topStart = 20.dp,topEnd = 20.dp)
                clip = true
            },
        backgroundColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        bottomNavItems.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(imageVector = item.icon, tint = Violet, contentDescription = item.title) },
                label = { Text(text = item.title, fontSize = 12.sp, fontWeight = FontWeight.Medium) },
                selectedContentColor = Pink,
                unselectedContentColor = Color.White,
                selected = currentRoute == item.route,
                alwaysShowLabel = false,
                onClick = {

                    if(currentRoute != item.route) {
                        navController.navigate(item.route) {
                            //used pop up to avoid stack in bottom navigation
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}

//TODO 1 : Change colors of selected and icon color