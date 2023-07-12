package com.sb.composepractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sb.composepractice.ui.Handle
import com.sb.composepractice.ui.core.GradientText
import com.sb.composepractice.ui.core.bottomsheetdialog.BottomSheetBehaviorProperties
import com.sb.composepractice.ui.core.bottomsheetdialog.BottomSheetDialog
import com.sb.composepractice.ui.core.bottomsheetdialog.BottomSheetUiProperties
import com.sb.composepractice.ui.core.bottomsheetdialog.SheetState
import com.sb.composepractice.ui.theme.ComposePracticeTheme
import com.sb.composepractice.ui.theme.DarkSeaBlue
import com.sb.composepractice.ui.theme.SeaBlue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePracticeTheme {
                var sheetState by remember {
                    mutableStateOf(SheetState.COLLAPSED)
                }
                val bottomSheetBehaviorProperties by remember {
                    mutableStateOf(
                        BottomSheetBehaviorProperties(
                            sheetState = sheetState,
                            onDismiss = {
                                sheetState = it
                            },
                            onShow = {
                                sheetState = it
                            }
                        )
                    )
                }

                BottomSheetDialog(
                    modifier = Modifier
                        .background(
                            Color.DarkGray,
                            RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                        ),
                    sheetState = sheetState,
                    content = {
                        GradientText(
                            modifier = Modifier
                                .height(600.dp),
                            text = "Hello Compose",
                            colors = listOf(
                                SeaBlue,
                                DarkSeaBlue
                            ),
                            onClick = {
                                println("Hello Compose")
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
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
                                    sheetState = SheetState.COLLAPSED
                                    println("Handle onClick >> ${sheetState}")
                                }
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            GradientText(
                                text = "Dialog Contents",
                                colors = listOf(Color.LightGray, Color.Gray)
                            )
                        }
                    },
                    uiProperties = BottomSheetUiProperties(
                        fullExpandedHeight = 400.dp
                    ),
                    behaviorProperties = bottomSheetBehaviorProperties
                )
            }
        }
    }
}