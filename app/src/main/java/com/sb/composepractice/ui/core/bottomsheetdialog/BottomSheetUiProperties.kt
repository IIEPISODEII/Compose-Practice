package com.sb.composepractice.ui.core.bottomsheetdialog

import androidx.annotation.FloatRange
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
data class BottomSheetUiProperties(
    val peekHeight: Dp = 30.dp,
    val halfExpandedHeight: Dp = 100.dp,
    val fullExpandedHeight: Dp,
    @FloatRange(from = 0.toDouble(), to = 1.toDouble(), fromInclusive = true, toInclusive = true) val backgroundDimmingRatio: Float = 0.4F,

)
