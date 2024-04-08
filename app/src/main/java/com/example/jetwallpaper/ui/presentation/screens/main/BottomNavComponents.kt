package com.example.jetwallpaper.ui.presentation.screens.main

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.jetwallpaper.navigation.Screens
import com.example.jetwallpaper.ui.theme.UiColors

sealed class BottomNavItem(val title: String, val icon: ImageVector, val route: String) {

    data object Explore : BottomNavItem("Explore", Icons.Outlined.Search, Screens.Explore.route)
    data object New : BottomNavItem("New", Icons.Outlined.LocalFireDepartment, Screens.New.route)
    data object Favourite :
        BottomNavItem("Favourite", Icons.Outlined.Favorite, Screens.Favourite.route)

}

val bottomNavItems = listOf(
    BottomNavItem.Explore,
    BottomNavItem.New,
    BottomNavItem.Favourite
)

@Composable
fun WallpaperBottomNavigationBar(navController: NavHostController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = bottomNavItems.map { it.route }
            .contains(navBackStackEntry?.destination?.route)

    if(showBottomBar){
        NavigationBar(
            modifier = Modifier
                .graphicsLayer {
                    shape = ShapeDefaults.Medium.run {
                        copy(
                            topStart = topStart, topEnd = topEnd,
                            CornerSize(0), CornerSize(0)
                        )
                    }
                    clip = true
                },
            containerColor = UiColors.BottomNavColor,
        ) {

            bottomNavItems.forEach { item ->
                NavigationBarItem(
                    icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                    label = { Text(text = item.title, fontWeight = FontWeight.Medium) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = UiColors.Pink,
                        selectedTextColor = UiColors.Pink,
                        unselectedIconColor = UiColors.Violet,
                        unselectedTextColor = Color.White
                    ),
                    selected = currentRoute == item.route,
                    alwaysShowLabel = false,
                    onClick = {

                        if (currentRoute != item.route) {
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
}
