package com.sb.composepractice.ui.core.circularboard

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CircularBoard(
    size: Int = 400,
    startAngle: Int = 0,
    endAngle: Int = 360,
    modifier: Modifier,
    list: List<Int>,
    onMenuItemClick: (Float, Int) -> Unit
) {
    require(startAngle < endAngle) { "EndAngle should be larger than StartAngle" }

    Layout(
        modifier = modifier
            .size(size.dp)
            .background(color = Color.Black),
        measurePolicy = { measurables, constraints ->
            val placeables = measurables.map {
                it.measure(
                    constraints.copy(
                        maxWidth = constraints.maxWidth,
                        minWidth = constraints.maxWidth,
                        maxHeight = constraints.maxHeight,
                        minHeight = constraints.maxHeight
                    )
                )
            }

            val centerX = constraints.maxWidth / 2
            val centerY = constraints.maxHeight / 2
            val theta = (endAngle - startAngle) / 180 * 3.1415927F / list.size

            layout(
                width = constraints.maxWidth,
                height = constraints.maxHeight,
            ) {

                placeables.forEachIndexed { index, placeable ->
                    val x = centerX + sin(theta * index) * (constraints.maxWidth / 2F)
                    val y = centerY - cos(theta * index) * (constraints.maxHeight / 2F)
                    placeable.place(x = 0, y = 0)
                }
            }
        },
        content = {
            Menu(
                size = size,
                itemList = list.map {it.toString() },
                onMenuItemClick = onMenuItemClick
            )
        }
    )
}

@Composable
internal fun Menu(
    size: Int,
    itemList: List<String>,
    animationDuration: Int = 2000,
    onMenuItemClick: (Float, Int) -> Unit = { angle: Float, idx: Int -> }
) {
    require(itemList.isNotEmpty())

    val density = LocalDensity.current.density
    val pxSize = size * density
    val sensitivity = 10

    var angle by remember { mutableFloatStateOf(0F) }
    var isTapped by remember { mutableStateOf(false) }
    val animatedAngle = remember { Animatable(0F) }
    LaunchedEffect(key1 = isTapped, key2 = angle) {
        if (isTapped) {
            animatedAngle.stop()
        }
        else animatedAngle.animateTo(
            targetValue = angle * sensitivity,
            animationSpec = tween(
                durationMillis = animationDuration,
                delayMillis = 0,
                easing = LinearOutSlowInEasing
            )
        )
    }
    val sweepAngle = remember { 360F / itemList.size }
    val colorList = List(itemList.size) {
        when {
            it.toFloat() <= itemList.size/3F -> {
                Color(
                    red = kotlin.math.abs((255 * cos(it * (3*Math.PI/2) / itemList.size)).toInt()),
                    green = kotlin.math.abs((255 * sin(it * (3*Math.PI/2) / itemList.size)).toInt()),
                    blue = 0
                )
            }
            it.toFloat() in itemList.size/3F..(itemList.size*2)/3F -> {
                Color(
                    red = 0,
                    green = (255 * sin(it*(3*Math.PI/2) / itemList.size)).toInt(),
                    blue = kotlin.math.abs((255 * cos(it * (3*Math.PI/2) / itemList.size)).toInt())
                )
            }
            else -> {
                Color(
                    red = kotlin.math.abs((255 * sin(it * (3*Math.PI/2) / itemList.size)).toInt()),
                    green = 0,
                    blue = kotlin.math.abs((255 * cos(it * (3*Math.PI/2) / itemList.size)).toInt())
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .size(size.dp)
            .drawBehind {
                repeat(itemList.size) {
                    drawArc(
                        startAngle = animatedAngle.value + sweepAngle * it,
                        sweepAngle = sweepAngle,
                        color = colorList[it],
                        useCenter = true
                    )
                }
            }
            .pointerInput(true) {
                detectDragGestures(
                    onDragStart = {},
                    onDrag = { change: PointerInputChange, dragAmount: Offset ->
                        isTapped = false
                        var oldAngle = atan2(
                            change.position.y - dragAmount.y - pxSize / 2F,
                            change.position.x - dragAmount.x - pxSize / 2F
                        ) * 180 / Math.PI
                        var newAngle = atan2(
                            change.position.y - pxSize / 2F,
                            change.position.x - pxSize / 2F
                        ) * 180 / Math.PI

                        if (newAngle in -180F..-90F && oldAngle in 90F..180F) newAngle += 360
                        if (oldAngle in -180F..-90F && newAngle in 90F..180F) oldAngle += 360
                        angle += (newAngle - oldAngle).toFloat()
                    }
                )
            }
            .pointerInput(true) {
                detectTapGestures(
                    onTap = {
                        // 탭한 위치의 각도
                        var tapPositionAngle = atan2(
                            it.y - pxSize / 2F,
                            it.x - pxSize / 2F
                        ) * 180 / Math.PI
                        if (tapPositionAngle < 0) tapPositionAngle += 360F

                        isTapped = true
                        val rotatedTapPositionAngle = (tapPositionAngle.toFloat() - animatedAngle.value%360).let { angle ->
                            if (angle < 0F) angle + 360F
                            else angle
                        }
                        val tapItemIdx = (rotatedTapPositionAngle * itemList.size / 360).toInt()
                        onMenuItemClick(rotatedTapPositionAngle, tapItemIdx)
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {}
}