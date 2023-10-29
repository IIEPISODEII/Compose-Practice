package com.sb.composepractice.ui.core.bottomsheetdialog

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min

@Composable
fun BottomSheetDialog(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    sheetContent: @Composable () -> Unit,
    properties: BottomSheetProperties,
) {
    if (properties.fullExpandedHeight < properties.peekHeight) throw IllegalArgumentException("expandedHeight must be bigger than peekHeight.")

    val density = LocalContext.current.resources.displayMetrics.density

    val hiddenStateEnabled = properties.peekHeight == 0.dp || properties.hiddenSheetStateEnabled
    val quaterExpandedHeight = (properties.peekHeight+properties.fullExpandedHeight) / 4F

    var offsetY by remember {
        mutableStateOf(
            when (properties.initialSheetState) {
                SheetState.COLLAPSED -> -properties.peekHeight
                SheetState.FULLY_EXPANDED -> -properties.fullExpandedHeight
                SheetState.HALF_EXPANDED -> -properties.halfExpandedHeight
                SheetState.HIDDEN -> 0.dp
            }
        )
    }
    var subOffsetY by remember {
        mutableStateOf(
            when (properties.initialSheetState) {
                SheetState.COLLAPSED -> -properties.peekHeight.value.toInt()
                SheetState.FULLY_EXPANDED -> -properties.fullExpandedHeight.value.toInt()
                SheetState.HALF_EXPANDED -> -properties.halfExpandedHeight.value.toInt()
                SheetState.HIDDEN -> 0
            }
        )
    }
    var sheetState by remember {
        mutableStateOf(properties.initialSheetState)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // offsetY 값이 변함에 따라 애니메이션을 통해 반영되는 offsetY 값
        val offsetYAnimated = animateFloatAsState(
            targetValue = when (sheetState) {
                SheetState.COLLAPSED -> -properties.peekHeight.value
                SheetState.FULLY_EXPANDED -> -properties.fullExpandedHeight.value
                SheetState.HALF_EXPANDED -> -properties.halfExpandedHeight.value
                SheetState.HIDDEN -> 0F
                                            },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMediumLow
            ),
            label = ""
        )

        Box(
            modifier = Modifier
                .wrapContentSize()
                .padding(bottom = if (sheetState != SheetState.HIDDEN) properties.peekHeight else 0.dp),
        ) {
            content()
            if (sheetState !in listOf(SheetState.COLLAPSED, SheetState.HIDDEN)) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .drawBehind {
                            drawRect(
                                color = Color.Gray,
                                alpha = if (properties.backgroundDimEnabled) {
                                    (min(
                                        -offsetYAnimated.value.dp,
                                        properties.fullExpandedHeight
                                    ) - properties.peekHeight) / (properties.fullExpandedHeight - properties.peekHeight) * properties.backgroundDimmingRatio
                                } else 0F,
                                style = Fill
                            )
                        }
                        .clickable {
                            if (properties.collapseContentByBackgroundTouchEnabled) {
                                when {
                                    offsetY < -quaterExpandedHeight -> {
                                        offsetY = -properties.peekHeight
                                        subOffsetY = offsetY.value.toInt()
                                        sheetState = SheetState.COLLAPSED
                                        properties.onDismiss(sheetState, offsetY.value)
                                    }

                                    else -> {
                                        if (hiddenStateEnabled) {
                                            offsetY = 0.dp
                                            subOffsetY = offsetY.value.toInt()
                                            sheetState = SheetState.HIDDEN
                                            properties.onDismiss(sheetState, offsetY.value)
                                        }
                                    }
                                }
                            }
                        }
                )
            }
        }

        if (sheetState != SheetState.HIDDEN) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(properties.fullExpandedHeight)
                    .offset {
                        IntOffset(
                            x = 0,
                            y = (density * (properties.fullExpandedHeight.value + offsetYAnimated.value)).toInt()
                        )
                    }
                    .nestedScroll(
                        connection =
//                        connection = if (!properties.nestedScrollConnectionEnabled) {
                            object : NestedScrollConnection {
                                override fun onPostScroll(
                                    consumed: Offset,
                                    available: Offset,
                                    source: NestedScrollSource
                                ): Offset {
                                    return Offset.Zero
                                }
                            },
//                        } else nestedScrollConnection,
                        dispatcher = NestedScrollDispatcher().apply {

                        }
                    )
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { _ -> },
                            onDragEnd = {
                                if (offsetY in -properties.fullExpandedHeight..-(quaterExpandedHeight * 3F)) {
                                    offsetY = -properties.fullExpandedHeight
                                    subOffsetY = offsetY.value.toInt()
                                    sheetState = SheetState.FULLY_EXPANDED
                                    return@detectDragGestures
                                }
                                if (offsetY in (-properties.peekHeight - quaterExpandedHeight)..-properties.peekHeight) {
                                    offsetY = -properties.peekHeight
                                    subOffsetY = offsetY.value.toInt()
                                    sheetState = SheetState.COLLAPSED
                                    return@detectDragGestures
                                }
                                if (properties.halfExpandedStateEnabled) {
                                    if (offsetY in -quaterExpandedHeight * 3F..-properties.peekHeight - quaterExpandedHeight) {
                                        offsetY = -properties.halfExpandedHeight
                                        subOffsetY = offsetY.value.toInt()
                                        sheetState = SheetState.HALF_EXPANDED
                                    }
                                } else {
                                    if (offsetY in -quaterExpandedHeight * 3F..-properties.peekHeight - quaterExpandedHeight) {
                                        offsetY = -properties.fullExpandedHeight
                                        subOffsetY = offsetY.value.toInt()
                                        sheetState = SheetState.FULLY_EXPANDED
                                    }
                                }
                            },
                            onDragCancel = { },
                            onDrag = { _, dragAmount ->
                                val delta = dragAmount.y

                                if (delta > 0) { // 아래로 드래그
                                    if (sheetState == SheetState.COLLAPSED || sheetState == SheetState.HIDDEN) return@detectDragGestures
                                    offsetY += delta.dp
                                    if (offsetY.value - subOffsetY.toFloat() !in -5F..5F) subOffsetY =
                                        offsetY.value.toInt()

                                    if (offsetY > -properties.peekHeight) {
                                        offsetY = -properties.peekHeight
                                        subOffsetY = offsetY.value.toInt()
                                        sheetState = SheetState.COLLAPSED
                                    }
                                } else { // 위로 드래그
                                    if (sheetState == SheetState.FULLY_EXPANDED) return@detectDragGestures
                                    offsetY += delta.dp
                                    if (offsetY.value - subOffsetY.toFloat() !in -5F..5F) subOffsetY =
                                        offsetY.value.toInt()

                                    if (offsetY < -properties.fullExpandedHeight) {
                                        offsetY = -properties.fullExpandedHeight
                                        subOffsetY = offsetY.value.toInt()
                                        sheetState = SheetState.FULLY_EXPANDED
                                    }
                                }
                            },
                        )
                    }
                    .align(Alignment.BottomCenter)
                    .then(modifier)
            ) {
                sheetContent()
            }
        }
    }
}