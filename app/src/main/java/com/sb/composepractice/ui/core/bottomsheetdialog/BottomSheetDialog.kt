package com.sb.composepractice.ui.core.bottomsheetdialog

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min

@Composable
fun BottomSheetDialog(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    content: @Composable () -> Unit,
    sheetContent: @Composable () -> Unit,
    uiProperties: BottomSheetUiProperties,
    behaviorProperties: BottomSheetBehaviorProperties = BottomSheetBehaviorProperties()
) {
    if (uiProperties.fullExpandedHeight < uiProperties.peekHeight) throw IllegalArgumentException("expandedHeight argument must be bigger than peekHeight argument.")

    val density = LocalContext.current.resources.displayMetrics.density

    val hiddenStateEnabled = uiProperties.peekHeight == 0.dp || behaviorProperties.hiddenSheetStateEnabled
    val quaterExpandedHeight = (uiProperties.peekHeight+uiProperties.fullExpandedHeight) / 4F
    var currentSheetState by remember(sheetState) {
        mutableStateOf(sheetState)
    }
    val offsetY by remember(currentSheetState) {
        mutableStateOf(
            when (currentSheetState) {
                SheetState.HIDDEN -> 0.dp
                SheetState.COLLAPSED -> -uiProperties.peekHeight
                SheetState.HALF_EXPANDED -> -uiProperties.halfExpandedHeight
                SheetState.FULLY_EXPANDED -> -uiProperties.fullExpandedHeight
            }
        )
    }

    BackHandler(enabled = currentSheetState == SheetState.FULLY_EXPANDED) {
        currentSheetState = SheetState.COLLAPSED
    }

    // offsetY 값이 변함에 따라 애니메이션을 통해 반영되는 offsetY 값
    val offsetYAnimated: Float by animateFloatAsState(
        targetValue = offsetY.value,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize(),
        ) {
            content()
            if (offsetY < -quaterExpandedHeight) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .drawBehind {
                            drawRect(
                                color = Color.Gray,
                                alpha = if (behaviorProperties.backgroundDimEnabled) {
                                    (min(
                                        -offsetYAnimated.dp,
                                        uiProperties.fullExpandedHeight
                                    ) - uiProperties.peekHeight) / (uiProperties.fullExpandedHeight - uiProperties.peekHeight) * uiProperties.backgroundDimmingRatio
                                } else 0F,
                                style = Fill
                            )
                        }
                        .clickable {
                            if (behaviorProperties.collapseContentByBackgroundTouch) {
                                when {
                                    offsetY < -quaterExpandedHeight -> {
                                        currentSheetState =
                                            if (hiddenStateEnabled) SheetState.HIDDEN else SheetState.COLLAPSED
                                        behaviorProperties.onDismiss(currentSheetState)
                                    }

                                    else -> {}
                                }
                            }
                        }
                )
            }
        }

        if (currentSheetState != SheetState.HIDDEN) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(uiProperties.fullExpandedHeight)
                    .offset {
                        IntOffset(
                            x = 0,
                            y = (density * (uiProperties.fullExpandedHeight.value + offsetYAnimated)).toInt()
                        )
                    }
                    .draggable(
                        state = rememberDraggableState { delta ->
                            if (delta == 0F) return@rememberDraggableState
                            if (delta > 0) { // 아래로 드래그
                                if (currentSheetState in listOf(
                                        SheetState.COLLAPSED,
                                        SheetState.HIDDEN
                                    )
                                ) return@rememberDraggableState
                                currentSheetState = when (currentSheetState) {
                                    SheetState.FULLY_EXPANDED -> SheetState.HALF_EXPANDED
                                    else -> SheetState.COLLAPSED
                                }
                                behaviorProperties.onDismiss(currentSheetState)
                            } else { // 위로 드래그
                                currentSheetState = when (currentSheetState) {
                                    SheetState.COLLAPSED, SheetState.HALF_EXPANDED -> {
                                        SheetState.FULLY_EXPANDED
                                    }

                                    SheetState.FULLY_EXPANDED -> {
                                        return@rememberDraggableState
                                    }

                                    else -> {
                                        return@rememberDraggableState
                                    }
                                }
                                if (currentSheetState == SheetState.FULLY_EXPANDED) behaviorProperties.onShow(currentSheetState)
                            }
                        },
                        orientation = Orientation.Vertical
                    )
                    .align(Alignment.BottomCenter)
                    .then(modifier)
            ) {
                sheetContent()
            }
        }
    }
}