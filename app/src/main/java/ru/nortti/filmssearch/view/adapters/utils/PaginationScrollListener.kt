package ru.nortti.filmssearch.view.adapters.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener(var layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        var visibleCount = layoutManager.childCount
        var totalItemCount = layoutManager.itemCount
        var firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
        if (!isLastPage() && !isLoading()) {
            if ((visibleCount + firstVisiblePosition) >= totalItemCount && firstVisiblePosition >= 0 && totalItemCount >= getTotalPageCount()) {
                loadMoreItems()
            }
        }

    }

    public abstract fun loadMoreItems()

    public abstract fun getTotalPageCount() : Int

    public abstract fun isLastPage() : Boolean

    public abstract fun isLoading() : Boolean

}