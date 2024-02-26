package com.sb.composepractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.sb.composepractice.ui.Navigation
import com.sb.composepractice.ui.theme.ComposePracticeTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposePracticeTheme {
                var previousClickTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
                val navController = rememberNavController()

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Button(
                        onClick = {
                            val currentClickTime = System.currentTimeMillis()
                            if (currentClickTime - previousClickTime < 100) return@Button

                            previousClickTime = currentClickTime
                            if (navController.currentBackStackEntry?.destination?.route != Screen.MainScreen.route) navController.popBackStack()
                        },
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(horizontal = 16.dp, vertical = 24.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Home, contentDescription = null)
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                    )
                    Navigation(navController = navController)
                }
            }
        }
    }
}