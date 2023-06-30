package com.bookmark.bookmark_oneday.presentation.screens.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.TokenInfo
import com.bookmark.bookmark_oneday.domain.usecase.UseCaseGetGoogleAccessToken
import com.bookmark.bookmark_oneday.domain.usecase.UseCaseSetUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val useCaseGetGoogleAccessToken: UseCaseGetGoogleAccessToken,
    private val useCaseSetUser: UseCaseSetUser
) : ViewModel() {
    private var prevProgress = 0

    private val _nickname = MutableStateFlow("")
    val nickname = _nickname.asStateFlow()

    private val _comment = MutableStateFlow("")
    val comment = _comment.asStateFlow()

    private val _profileImageUrl = MutableStateFlow<String?>(null)
    val profileImageUrl = _profileImageUrl.asStateFlow()

    private val _goalReadingTimeMinute = MutableStateFlow(120)
    val goalReadingTimeMinute = _goalReadingTimeMinute.asStateFlow()

    private val _showLoadingDialog = MutableStateFlow<Boolean>(false)
    val showLoadingDialog = _showLoadingDialog.asStateFlow()

    private val _loginSuccess = MutableSharedFlow<Boolean>()
    val loginSuccess = _loginSuccess.asSharedFlow()

    private val _getGoogleAccessTokenSuccess = MutableSharedFlow<Boolean>()
    val getGoogleAccessTokenSuccess = _getGoogleAccessTokenSuccess.asSharedFlow()

    private var tempTokenInfo : TokenInfo ?= null

    fun setPrevProgress(progress : Int)  { prevProgress = progress }

    fun getPrevProgress() = prevProgress

    fun setGoalReadingTimeMinute(minute : Int) { _goalReadingTimeMinute.value = minute }

    fun setNickname(nickname : String) { _nickname.value = nickname }

    fun setComment(comment : String) { _comment.value = comment }

    fun setProfileImageUrl(url : String) { _profileImageUrl.value = url }

    fun clearCommentAndProfile() {
        _profileImageUrl.value = null
        _comment.value = ""
    }

    fun trySignup() {
        viewModelScope.launch {
            _showLoadingDialog.value = true

            useCaseSetUser.setUserProfile(nickname = nickname.value, profileUri = profileImageUrl.value, bio = comment.value)
            useCaseSetUser.setReadingTime(readingTime = goalReadingTimeMinute.value)

            delay(1000L)
            _loginSuccess.emit(true)
            _showLoadingDialog.value = false
        }
    }

    fun tryGetGoogleAccessToken(authCode : String) {
        viewModelScope.launch {
            _showLoadingDialog.value = true

            val response = useCaseGetGoogleAccessToken(authCode)
            if (response is BaseResponse.Success) {
                tempTokenInfo = response.data
            }

            _getGoogleAccessTokenSuccess.emit(response is BaseResponse.Success)
            _showLoadingDialog.value = false
        }
    }
}