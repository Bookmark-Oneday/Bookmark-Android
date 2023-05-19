package com.bookmark.bookmark_oneday.presentation.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.View

fun Context.applyStatusBarPadding(view : View) {
    val statusBarHeight = ResourceDimenCache.getStatusBarHeight(this)
    view.setPadding(view.paddingLeft, view.paddingTop + statusBarHeight, view.paddingRight, view.paddingBottom)
}

/**
 * statusBar와 같은 resource의 dimen을 구하는 과정에서 reflection이 발생하므로,
 * 한번 구한 값을 캐싱하여 재사용하기 위해 사용되는 싱글톤 객체
 */
@SuppressLint("InternalInsetResource", "DiscouragedApi")
internal object ResourceDimenCache {
    private const val NOT_INIT = -1
    private var statusBarHeight = NOT_INIT

    fun getStatusBarHeight(context : Context) : Int {
        if (statusBarHeight == NOT_INIT) {
            statusBarHeight = context.run {
                val resourceId = resources.getIdentifier("status_bar_height", "dimen",  "android")
                if (resourceId <= 0) return@run 0

                resources.getDimensionPixelSize(resourceId)
            }
        }

        return statusBarHeight
    }
}