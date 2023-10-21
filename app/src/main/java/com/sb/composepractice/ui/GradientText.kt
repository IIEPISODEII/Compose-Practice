package com.sb.composepractice.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sb.composepractice.ui.theme.DarkSeaBlue
import com.sb.composepractice.ui.theme.SeaBlue


@Composable
fun GradientText(
    modifier: Modifier = Modifier,
    text: String,
    fontWeight: FontWeight = FontWeight.Normal,
    style: TextStyle = TextStyle(
        color = Color.White,
        fontSize = 28.sp
    ),
    textAlign: TextAlign = TextAlign.Center,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = 1,
    onClick: () -> Unit = {},
    colors: List<Color>
) {
    Column(
        modifier = Modifier
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var composableSize by remember {
            mutableStateOf(DpSize.Zero)
        }
        Spacer(
            modifier = Modifier
                .height(4.dp)
                .fillMaxWidth()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9F)
                .height(300.dp)
                .onSizeChanged { size ->
                    composableSize = DpSize(width = size.width.dp, height = size.height.dp)
                }
                .background(
                    brush = Brush.linearGradient(
                        colors = colors,
                        start = Offset(
                            x = 0.dp.value,
                            y = 0.dp.value
                        ),
                        end = Offset(
                            x = 0.dp.value,
                            y = composableSize.height.value
                        )
                    ),
                    shape = RoundedCornerShape(12.dp),
                    alpha = 0.75F
                )
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                    onClick.invoke()
                },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(MaterialTheme.shapes.medium),
                text = text,
                textAlign = textAlign,
                fontWeight = fontWeight,
                style = style,
                overflow = overflow,
                maxLines = maxLines,
            )
        }
        Spacer(
            modifier = Modifier
                .height(4.dp)
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun Preview() {
    GradientText(
        text = "Hello world",
        colors = listOf(
            SeaBlue,
            DarkSeaBlue
        )
    )
}