package com.sb.composepractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sb.composepractice.ui.Handle
import com.sb.composepractice.ui.PersonProfile
import com.sb.composepractice.ui.core.bottomsheetdialog.BottomSheetDialog
import com.sb.composepractice.ui.core.bottomsheetdialog.BottomSheetProperties
import com.sb.composepractice.ui.core.bottomsheetdialog.SheetState
import com.sb.composepractice.ui.core.waitingdotanimation.WaitingDotAnimationBox
import com.sb.composepractice.ui.theme.ComposePracticeTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePracticeTheme {
                var contentVisible by remember {
                    mutableStateOf(false)
                }
                LaunchedEffect(key1 = Unit) {
                    delay(3000L)
                    contentVisible = true
                }

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
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0xFF9F9F9F), Color(0xFFBBBBBB)),
                                startY = 0F,
                                endY = 0.4F
                            ),
                            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                        ),
                    content = {},
                    sheetContent =  {
                        if (contentVisible) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
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
                                LazyColumn(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                ) {
                                    items(10) {
                                        PersonProfile()
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                WaitingDotAnimationBox(
                                    dotRadius = 12.dp,
                                    dotDistance = 36.dp
                                )
                            }
                        }
                    },
                    properties = bottomSheetProperties
                )
            }
        }
    }
}