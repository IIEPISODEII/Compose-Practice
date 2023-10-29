package com.sb.composepractice.ui.core.waitingdotanimation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun WaitingDotAnimationBox(
    dotRadius: Dp = 4.dp,
    dotDistance: Dp = 12.dp
) {
    val density = LocalContext.current.resources.displayMetrics.density

    val animatedDxRatio = remember { Animatable(0F) }

    LaunchedEffect(key1 = Unit) {
        while (true) {
            animatedDxRatio.snapTo(0F)
            animatedDxRatio.animateTo(
                targetValue  = 1F,
                animationSpec = tween(durationMillis = 1000)
            )
            delay(100L)
            animatedDxRatio.animateTo(
                targetValue  = 2F,
                animationSpec = tween(durationMillis = 1000)
            )
            delay(100L)
            animatedDxRatio.animateTo(
                targetValue  = 3F,
                animationSpec = tween(durationMillis = 1000)
            )
            delay(100L)
        }
    }

    Box(
        modifier = Modifier
            .width(dotDistance*3)
            .height(dotRadius*2)
            .background(color = Color.Transparent)
            .drawWithContent {
                // 1st circle
                drawCircle(
                    brush = Brush.horizontalGradient(listOf(Color.Red, Color.Red)),
                    radius = when (animatedDxRatio.value) {
                        in 0F..1F -> animatedDxRatio.value
                        in 1F..2F -> 1F
                        else -> 3-animatedDxRatio.value
                    } * density * dotRadius.value,
                    center = Offset(
                        x = (dotDistance.value*1.5F + (animatedDxRatio.value-1.5F) * dotDistance.value) * density,
                        y = dotRadius.value * density
                    )
                )

                // 2nd circle
                drawCircle(
                    brush = Brush.horizontalGradient(listOf(Color.Green, Color.Green)),
                    radius = when (animatedDxRatio.value) {
                        in 0F..1F -> 1F
                        in 1F..2F -> 2-animatedDxRatio.value
                        else -> animatedDxRatio.value-2
                    } * density * dotRadius.value,
                    center = Offset(
                        x = (dotDistance.value*1.5F + when(animatedDxRatio.value) {
                            in 0F..2F -> animatedDxRatio.value-1.5F+1F
                            else -> animatedDxRatio.value-2F-1.5F
                        } * dotDistance.value) * density,
                        y = dotRadius.value * density
                    )
                )

                // 3rd circle
                drawCircle(
                    brush = Brush.horizontalGradient(listOf(Color.Blue, Color.Blue)),
                    radius = when (animatedDxRatio.value) {
                        in 0F..1F -> 1-animatedDxRatio.value
                        in 1F..2F -> animatedDxRatio.value-1
                        else -> 1F
                    } * density * dotRadius.value,
                    center = Offset(
                        x = (dotDistance.value*1.5F + when(animatedDxRatio.value) {
                            in 0F..1F -> animatedDxRatio.value+0.5F
                            else -> animatedDxRatio.value-2.5F
                        } * dotDistance.value) * density,
                        y = dotRadius.value * density
                    )
                )
            }
    )
}

@Preview
@Composable
fun Preview() {
    WaitingDotAnimationBox()
}