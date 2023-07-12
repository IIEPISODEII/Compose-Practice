package com.sb.composepractice.ui.core.bottomsheetdialog

import androidx.compose.runtime.Stable

@Stable
enum class SheetState {
    FULLY_EXPANDED,
    HALF_EXPANDED,
    COLLAPSED,
    HIDDEN
}