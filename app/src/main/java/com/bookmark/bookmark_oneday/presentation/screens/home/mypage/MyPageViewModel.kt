package com.bookmark.bookmark_oneday.presentation.screens.home.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.domain.model.UserInfo
import com.bookmark.bookmark_oneday.domain.usecase.UseCaseGetUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val useCaseGetUser: UseCaseGetUser
) : ViewModel() {
    val user = useCaseGetUser.getProfile().stateIn(
        initialValue = UserInfo.defaultUserInfo,
        started = SharingStarted.WhileSubscribed(),
        scope = viewModelScope
    )
}