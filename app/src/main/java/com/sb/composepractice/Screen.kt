package com.sb.composepractice

sealed class Screen(val route: String) {
    object MainScreen: Screen("Main")
    object CircularBoardScreen: Screen("CircularBoard")
    object BottomSheetDialogScreen: Screen("BottomSheetDialog")
}