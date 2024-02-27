package com.sb.composepractice.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sb.composepractice.ui.core.circularboard.CircularBoard

@Composable
fun NavCircularBoardScreen() {

    var circularMenuTapAngle by remember { mutableFloatStateOf(0F) }
    var circularMenuTapItemIdx by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CircularBoard(
            itemList = List(9) { it },
            onMenuItemClick = { angle, idx ->
                circularMenuTapAngle = angle
                circularMenuTapItemIdx = idx
            }
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            text = "${circularMenuTapAngle}도 위치 클릭",
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            text = "${circularMenuTapItemIdx+1}번째 메뉴 클릭",
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )
    }
}