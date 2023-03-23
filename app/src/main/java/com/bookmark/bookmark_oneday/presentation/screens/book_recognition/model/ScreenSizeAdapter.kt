package com.bookmark.bookmark_oneday.presentation.screens.book_recognition.model

import android.graphics.Rect

class ScreenSizeAdapter {
    private var originalHeightPx : Int = 0
    private var originalWidthPx : Int = 0
    private var boundingBoxHeightPx : Int = 0
    private var boundingBoxWidthPx : Int = 0
    private var cameraResolutionHeight : Int = 0
    private var cameraResolutionWidth : Int = 0

    private var adaptiveHeightPx = 0f
    private var adaptiveWidthPx = 0f

    private var adaptiveBoundingBoxHeight = 0
    private var adaptiveBoundingBoxWidth = 0

    private val boundingBoxRect = Rect()

    fun setCameraResolutionInfo(heightPx : Int, widthPx : Int) {
        cameraResolutionHeight = heightPx
        cameraResolutionWidth = widthPx

        if (checkAllInitialize()) {
            setBoundingBox()
        }
    }

    fun setViewSizeInfo(totalHeightPx : Int, totalWidthPx : Int, targetHeightPx : Int, targetWidthPx : Int) {
        originalHeightPx = totalHeightPx
        originalWidthPx = totalWidthPx
        boundingBoxHeightPx = targetHeightPx
        boundingBoxWidthPx = targetWidthPx

        if (checkAllInitialize()) {
            setBoundingBox()
        }
    }

    fun checkAllInitialize() : Boolean {
        return (originalHeightPx != 0 && originalWidthPx != 0
                && boundingBoxHeightPx != 0 && boundingBoxWidthPx != 0
                && cameraResolutionHeight != 0 && cameraResolutionWidth != 0)
    }

    private fun setBoundingBox() {
        val originalRatio = originalHeightPx / originalWidthPx.toFloat()
        val cameraResolutionRatio = cameraResolutionHeight / cameraResolutionWidth.toFloat()

        // 카메라에서 사용하는 해상도가 화면보다 세로로 긴 경우
        if (originalRatio > cameraResolutionRatio) {
            adaptiveHeightPx = originalHeightPx.toFloat()
            adaptiveWidthPx = (originalWidthPx * originalRatio / cameraResolutionRatio)
        } else {
            adaptiveHeightPx = (originalHeightPx * cameraResolutionRatio / originalRatio)
            adaptiveWidthPx = originalWidthPx.toFloat()
        }

        val heightScaleFactor = boundingBoxHeightPx / adaptiveHeightPx
        val widthScaleFactor = boundingBoxWidthPx / adaptiveWidthPx

        adaptiveBoundingBoxHeight = (cameraResolutionHeight * heightScaleFactor).toInt()
        adaptiveBoundingBoxWidth = (cameraResolutionWidth * widthScaleFactor).toInt()

        boundingBoxRect.set(
            cameraResolutionWidth / 2 - adaptiveBoundingBoxWidth / 2,
            cameraResolutionHeight / 2 - adaptiveBoundingBoxHeight / 2,
            cameraResolutionWidth / 2 + adaptiveBoundingBoxWidth / 2,
            cameraResolutionHeight / 2 + adaptiveBoundingBoxHeight / 2
        )
    }

    override fun toString(): String {
        return "width : $adaptiveBoundingBoxWidth, height : $adaptiveBoundingBoxHeight"
    }

    fun checkInBoundingBox(rect : Rect) : Boolean {
          return boundingBoxRect.contains(rect)
    }
}