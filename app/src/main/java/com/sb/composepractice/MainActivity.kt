package com.sb.composepractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sb.composepractice.ui.GradientText
import com.sb.composepractice.ui.Handle
import com.sb.composepractice.ui.core.bottomsheetdialog.BottomSheetDialog
import com.sb.composepractice.ui.core.bottomsheetdialog.BottomSheetProperties
import com.sb.composepractice.ui.core.bottomsheetdialog.SheetState
import com.sb.composepractice.ui.theme.ComposePracticeTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePracticeTheme {

                val bottomSheetProperties by remember {
                    mutableStateOf(
                        BottomSheetProperties(
                            fullExpandedHeight = 400.dp,
                            onDismiss = { sheetState, _ ->
                                if (sheetState == SheetState.COLLAPSED) {
                                    println("COLLAPSED")
                                }
                            },
                            halfExpandedHeight = 200.dp,
                            halfExpandedStateEnabled = true,
                            hiddenSheetStateEnabled = true,
                            nestedScrollConnectionEnabled = true,
                            initialSheetState = SheetState.FULLY_EXPANDED,
                            sheetContentScrollEnabled = true,
                            sheetContentScrollState = null
                        )
                    )
                }

                BottomSheetDialog(
                    modifier = Modifier
                        .background(
                            Color.DarkGray,
                            RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                        ),
                    content = {
                        LazyColumn() {
                            items(10) {
                                GradientText(
                                    text = it.toString(),
                                    colors = listOf(
                                        Color(
                                            red = Random.nextInt(0, 255),
                                            blue = Random.nextInt(0, 255),
                                            green = Random.nextInt(0, 255)
                                        ),
                                        Color(
                                            red = Random.nextInt(0, 255),
                                            blue = Random.nextInt(0, 255),
                                            green = Random.nextInt(0, 255)
                                        )
                                    )
                                )
                            }
                        }
                    },
                    sheetContent =  {
                        Column(
                            modifier = Modifier
                                .wrapContentSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Handle(
                                color = Color.White,
                                onClick = {
                                    bottomSheetProperties.onDismiss(SheetState.COLLAPSED, bottomSheetProperties.peekHeight.value)
                                }
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            LazyColumn() {
                                items(10) {
                                    GradientText(
                                        text = it.toString(),
                                        colors = listOf(
                                            Color(
                                                red = Random.nextInt(0, 255),
                                                blue = Random.nextInt(0, 255),
                                                green = Random.nextInt(0, 255)
                                            ),
                                            Color(
                                                red = Random.nextInt(0, 255),
                                                blue = Random.nextInt(0, 255),
                                                green = Random.nextInt(0, 255)
                                            )
                                        )
                                    )
                                }
                            }
                        }
                    },
                    properties = bottomSheetProperties
                )
            }
        }
    }
}