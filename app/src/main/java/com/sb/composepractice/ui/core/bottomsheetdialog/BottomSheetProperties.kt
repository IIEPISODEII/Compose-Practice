package com.sb.composepractice.ui.core.bottomsheetdialog

import androidx.annotation.FloatRange
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class BottomSheetProperties(
    val peekHeight: Dp = 30.dp,
    val halfExpandedHeight: Dp = 100.dp,
    val fullExpandedHeight: Dp,
    @FloatRange(from = 0.toDouble(), to = 1.toDouble(), fromInclusive = true, toInclusive = true) val backgroundDimmingRatio: Float = 0.4F,
    val initialSheetState: SheetState = SheetState.COLLAPSED,
    val collapseContentByBackgroundTouchEnabled: Boolean = true,
    val backgroundDimEnabled: Boolean = true,
    val hiddenSheetStateEnabled: Boolean = false,
    val halfExpandedStateEnabled: Boolean = false,
    val nestedScrollConnectionEnabled: Boolean = false,
    val onDismiss: (SheetState, Float) -> Unit = { _, _ -> },
    val onShow: (SheetState, Float) -> Unit = { _, _ -> },
    val sheetContentScrollEnabled: Boolean = false,
    val sheetContentScrollState: ScrollState? = null
)
