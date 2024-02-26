package com.sb.composepractice.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sb.composepractice.Screen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        composable(
            route = Screen.MainScreen.route
        ) {
            NavMainScreen(navController = navController)
        }
        composable(
            route = Screen.CircularBoardScreen.route
        ) {
            NavCircularBoardScreen()
        }
        composable(
            route = Screen.BottomSheetDialogScreen.route
        ) {
            NavBottomSheetDialogScreen()
        }
    }
}