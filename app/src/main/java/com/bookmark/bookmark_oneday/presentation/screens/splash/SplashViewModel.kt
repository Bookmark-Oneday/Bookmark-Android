package com.bookmark.bookmark_oneday.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.domain.usecase.UseCaseCheckFirstExecuted
import com.bookmark.bookmark_oneday.domain.user.usecase.UseCaseGetUser
import com.bookmark.bookmark_oneday.presentation.screens.splash.model.NextAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val useCaseCheckFirstExecuted: UseCaseCheckFirstExecuted,
    private val useCaseGetUser: UseCaseGetUser
) : ViewModel() {

    private val _nextDest = MutableSharedFlow<NextAction>()
    val nextDest = _nextDest.asSharedFlow()

    fun checkFirstOrLogin() {
        viewModelScope.launch {
            val firstExecuted = useCaseCheckFirstExecuted.invoke()
            val isLogin = useCaseGetUser.isLogin().first()

            val nextAction = if (firstExecuted) {
                NextAction.INTRO
            } else if (isLogin) {
                NextAction.LOGIN
            } else {
                NextAction.HOME
            }

            delay(1000L)

            _nextDest.emit(nextAction)

        }

    }

}