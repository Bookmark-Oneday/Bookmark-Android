package com.bookmark.bookmark_oneday.presentation.screens.home.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.domain.user.model.UserInfo
import com.bookmark.bookmark_oneday.domain.user.usecase.UseCaseClearUser
import com.bookmark.bookmark_oneday.domain.user.usecase.UseCaseGetUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    useCaseGetUser: UseCaseGetUser,
    private val useCaseClearUser: UseCaseClearUser
) : ViewModel() {
    val user = useCaseGetUser.getProfile().stateIn(
        initialValue = UserInfo.defaultUserInfo,
        started = SharingStarted.WhileSubscribed(),
        scope = viewModelScope
    )

    fun clearData() {
        viewModelScope.launch {
            useCaseClearUser.invoke()
        }
    }
}