package com.sb.composepractice.ui.core.bottomsheetdialog

import android.content.DialogInterface
import androidx.compose.runtime.Stable

@Stable
data class BottomSheetBehaviorProperties(
    var sheetState: SheetState = SheetState.COLLAPSED,
    val collapseContentByBackgroundTouch: Boolean = true,
    val backgroundDimEnabled: Boolean = true,
    val hiddenSheetStateEnabled: Boolean = false,
    val halfExpandedStateEnabled: Boolean = false,
    val onDismiss: (SheetState) -> Unit = {},
    val onShow: (SheetState) -> Unit = {}
) {
    fun modifySheetState(to: SheetState) {
        if (sheetState != to) sheetState = to
    }

    fun hideSheet() {
        if (sheetState != SheetState.HIDDEN) sheetState = SheetState.HIDDEN
    }
}
