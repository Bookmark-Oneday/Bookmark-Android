package com.bookmark.bookmark_oneday.presentation.screens.intro

import androidx.lifecycle.ViewModel
import com.bookmark.bookmark_oneday.domain.appinfo.usecase.UseCaseSetExecuted
import com.bookmark.bookmark_oneday.presentation.screens.intro.model.IntroData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val useCaseSetExecuted: UseCaseSetExecuted
) : ViewModel() {
    val introDataList = IntroData.defaultIntroDataList

    private val _dotPosition = MutableStateFlow(0)
    val dotPosition = _dotPosition.asStateFlow()

    fun setExecuted() {
        useCaseSetExecuted()
    }

    fun setActivePosition(position : Int) {
        _dotPosition.value = position
    }
}