package tech.cognix.sauntatonttu.utils

import android.content.Context
import android.view.View
import android.widget.ProgressBar

class ProgressBarUtils(private val context: Context, private val progressBar: ProgressBar) {

    init {
        hideProgressBar()
    }

    fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
        progressBar.isClickable = false
        progressBar.isFocusable = false
    }

    fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }
}
