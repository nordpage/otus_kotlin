package ru.nortti.filmssearch.view.adapters.utils

import android.R
import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Context
import android.view.animation.BounceInterpolator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator


class ItemAnimator(var context: Context) : SimpleItemAnimator() {
    override fun animateAdd(holder: RecyclerView.ViewHolder?): Boolean {
        val set: Animator = AnimatorInflater.loadAnimator(
            context,
            R.animator.fade_in
        )
        set.setInterpolator(BounceInterpolator())
        set.setTarget(holder!!.itemView)
        set.start()

        return true
    }

    override fun runPendingAnimations() {

    }

    override fun animateMove(
        holder: RecyclerView.ViewHolder?,
        fromX: Int,
        fromY: Int,
        toX: Int,
        toY: Int
    ): Boolean {
        return false
    }

    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder?,
        newHolder: RecyclerView.ViewHolder?,
        fromLeft: Int,
        fromTop: Int,
        toLeft: Int,
        toTop: Int
    ): Boolean {
        return false
    }

    override fun isRunning(): Boolean {
        return false
    }

    override fun endAnimation(item: RecyclerView.ViewHolder) {

    }

    override fun animateRemove(holder: RecyclerView.ViewHolder?): Boolean {
        return false
    }

    override fun endAnimations() {

    }
}