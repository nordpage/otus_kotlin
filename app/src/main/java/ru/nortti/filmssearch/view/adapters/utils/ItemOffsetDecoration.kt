package ru.nortti.filmssearch.view.adapters.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemOffsetDecoration(var offset: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
            outRect.right = offset
            outRect.left = offset
            outRect.top = offset
            outRect.bottom = offset
    }

}