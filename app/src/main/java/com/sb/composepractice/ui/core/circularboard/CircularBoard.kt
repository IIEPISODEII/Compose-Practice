package com.sb.composepractice.ui.core.circularboard

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toOffset
import kotlinx.coroutines.delay
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun CircularBoard(
    radius: Int = 150,
    startAngle: Int = 0,
    endAngle: Int = 360,
    itemList: List<Int>,
    animationDuration: Int = 2000,
    onMenuItemClick: (Float, Int) -> Unit
) {
    require(startAngle < endAngle) { "EndAngle should be larger than StartAngle" }

    require(itemList.isNotEmpty())

    println("현재 너비: ${LocalDensity.current.density * 2 * radius} vs ${LocalConfiguration.current.screenWidthDp * LocalDensity.current.density}")

    val density = LocalDensity.current.density
    val radiusInPx = radius * density
    val sensitivity = 10

    var angle by remember { mutableFloatStateOf(0F) }
    var tapCounter by remember { mutableStateOf(false) }
    var tappedPosition by remember { mutableStateOf(Offset(0F, 0F)) }
    val animatedAngle = remember { Animatable(0F) }
    val sweepAngle = remember { 360F / itemList.size }
    val interactionSource= remember { MutableInteractionSource() }
    LaunchedEffect(key1 = tapCounter) {
        animatedAngle.stop()
        val tapEvent = PressInteraction.Press(tappedPosition)
        interactionSource.emit(tapEvent)
        delay(33L)
        interactionSource.emit(PressInteraction.Release(tapEvent))
    }
    LaunchedEffect(key1 = angle) {
        animatedAngle.animateTo(
            targetValue = angle * sensitivity,
            animationSpec = tween(
                durationMillis = animationDuration,
                delayMillis = 0,
                easing = LinearOutSlowInEasing
            )
        )
    }
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
            .wrapContentSize()
    ) {
        Box(
            modifier = Modifier
                .size((radius*2).dp)
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
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {},
                        onDrag = { change: PointerInputChange, dragAmount: Offset ->
                            var oldAngle = atan2(
                                change.position.y - dragAmount.y - radiusInPx,
                                change.position.x - dragAmount.x - radiusInPx
                            ) * 180 / Math.PI
                            var newAngle = atan2(
                                change.position.y - radiusInPx,
                                change.position.x - radiusInPx
                            ) * 180 / Math.PI

                            if (newAngle in -180F..-90F && oldAngle in 90F..180F) newAngle += 360
                            if (oldAngle in -180F..-90F && newAngle in 90F..180F) oldAngle += 360
                            angle += (newAngle - oldAngle).toFloat()
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            // 탭한 위치의 각도
                            var tapPositionAngle = atan2(
                                it.y - radiusInPx,
                                it.x - radiusInPx
                            ) * 180 / Math.PI
                            if (tapPositionAngle < 0) tapPositionAngle += 360F
                            tappedPosition = Offset(x = it.x, it.y)

                            tapCounter = !tapCounter
                            val rotatedTapPositionAngle =
                                (tapPositionAngle.toFloat() - animatedAngle.value % 360).let { angle ->
                                    if (angle < 0F) angle + 360F
                                    else angle % 360
                                }
                            val tapItemIdx = (rotatedTapPositionAngle * itemList.size / 360).toInt()
                            onMenuItemClick(rotatedTapPositionAngle, tapItemIdx)
                        }
                    )
                }
        )

        Box(
            modifier = Modifier
                .size(32.dp)
                .offset {
                    IntOffset(
                        x = (tappedPosition.x - 16*density).roundToInt(),
                        y = (tappedPosition.y - 16*density).roundToInt()
                    )
                }
                .indication(
                    interactionSource = interactionSource,
                    indication = rememberRipple(
                        bounded = false,
                        radius = 16.dp,
                        color = Color.White
                    )
                )
        )
    }
}