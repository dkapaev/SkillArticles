package ru.skillbranch.skillarticles.extensions

import android.view.View
import android.view.ViewGroup

// set optional margins in px
fun View.setMarginOptionally(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) {
    val params: ViewGroup.LayoutParams = this.layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        if (left != null) { params.leftMargin = left }
        if (top != null) { params.topMargin = top }
        if (right != null) { params.rightMargin = right }
        if (bottom != null) { params.bottomMargin = bottom }
    }
}
