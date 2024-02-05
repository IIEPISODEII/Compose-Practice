package com.sb.composepractice.ui.core

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CircularMenu(
    size: Int = 400,
    startAngle: Int = 0,
    endAngle: Int = 360,
    modifier: Modifier,
    list: List<Int>,
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
                onMenuItemClick = { idx -> println("클릭 포지션: $idx")}
            )
        }
    )
}

@Composable
internal fun Menu(
    size: Int,
    itemList: List<String>,
    animationDuration: Int = 2000,
    onMenuItemClick: (Int) -> Unit = {}
) {
    require(itemList.isNotEmpty())

    val density = LocalDensity.current.density
    val pxSize = size * density
    val sensitivity = 10

    var angle by remember { mutableFloatStateOf(0F) }
    val animatedAngle by animateFloatAsState(
        targetValue = angle*sensitivity,
        label = "",
        animationSpec = tween(durationMillis = animationDuration, delayMillis = 0, easing = LinearOutSlowInEasing)
    )
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
                        startAngle = animatedAngle + sweepAngle*it,
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
                        var tapPositionAngle = atan2(
                            it.y - pxSize / 2F,
                            it.x - pxSize / 2F
                        ) * 180 / Math.PI
                        if (tapPositionAngle < 0) tapPositionAngle += 360F

                        var angleDifference = tapPositionAngle - angle * sensitivity
                        while (angleDifference < 0) angleDifference += 360
                        val tapIndex = (angleDifference/sweepAngle).toInt()

                        onMenuItemClick(tapIndex)
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {}
}

@Composable
@Preview
fun Preview() {
    CircularMenu(modifier = Modifier, list = listOf(1, 2, 3))
}