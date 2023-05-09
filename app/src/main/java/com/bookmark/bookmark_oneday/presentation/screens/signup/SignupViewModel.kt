package com.bookmark.bookmark_oneday.presentation.screens.signup

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SignupViewModel : ViewModel() {
    private var prevProgress = 0

    private val _nickname = MutableStateFlow("")
    val nickname = _nickname.asStateFlow()

    private val _comment = MutableStateFlow("")
    val comment = _comment.asStateFlow()

    private val _profileImageUrl = MutableStateFlow<String?>(null)
    val profileImageUrl = _profileImageUrl.asStateFlow()

    private val _goalReadingTimeMinute = MutableStateFlow(120)
    val goalReadingTimeMinute = _goalReadingTimeMinute.asStateFlow()

    fun setPrevProgress(progress : Int)  {
        prevProgress = progress
    }

    fun getPrevProgress() = prevProgress

    fun setGoalReadingTimeMinute(minute : Int) {
        _goalReadingTimeMinute.value = minute
    }

    fun setNickname(nickname : String) {
        _nickname.value = nickname
    }

    fun setComment(comment : String) {
        _comment.value = comment
    }

    fun setProfileImageUrl(url : String) {
        _profileImageUrl.value = url
    }

    fun clearCommentAndProfile() {
        _profileImageUrl.value = null
        _comment.value = ""
    }
}