package com.bookmark.bookmark_oneday.presentation.screens.home.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.domain.user.model.UserInfo
import com.bookmark.bookmark_oneday.domain.user.usecase.UseCaseClearUser
import com.bookmark.bookmark_oneday.domain.user.usecase.UseCaseGetUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    useCaseGetUser: UseCaseGetUser,
    private val useCaseClearUser: UseCaseClearUser
) : ViewModel() {

    private val _clearDataSuccess = MutableSharedFlow<Boolean>()
    val clearDataSuccess = _clearDataSuccess.asSharedFlow()

    val user = useCaseGetUser.getProfile().stateIn(
        initialValue = UserInfo.defaultUserInfo,
        started = SharingStarted.WhileSubscribed(),
        scope = viewModelScope
    )

    fun clearData() {
        viewModelScope.launch(Dispatchers.IO) {
            useCaseClearUser.invoke()
            _clearDataSuccess.emit(true)
        }
    }
}