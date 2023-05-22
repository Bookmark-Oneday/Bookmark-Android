package com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.write

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.domain.model.BookItem
import com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.write.model.Position
import com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.write.model.TodayOnelineWriteScreenState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WriteTodayOnelineWriteViewModel @AssistedInject constructor(
    @Assisted val book : BookItem
) : ViewModel() {

    private val _finishCall = MutableSharedFlow<Boolean>()
    val finishCall = _finishCall.asSharedFlow()

    private val _content = MutableStateFlow("")
    val content = _content.asStateFlow()

    private val _currentState = MutableStateFlow<TodayOnelineWriteScreenState>(TodayOnelineWriteScreenState.TextMove)
    val currentState = _currentState.asStateFlow()

    private val _textColor = MutableStateFlow("#000000")
    val textColor = _textColor.asStateFlow()

    private val _backgroundUri = MutableStateFlow<String?>(null)
    val backgroundUri = _backgroundUri.asStateFlow()

    private val _textSize = MutableStateFlow(18)
    val textSize = _textSize.asStateFlow()

    private val _position = MutableStateFlow(Position(0.5f, 0.5f))
    val position = _position.asStateFlow()

    fun setTextColor(colorString: String) {
        _textColor.value = colorString
    }

    fun setText(text : String) {
        _content.value = text
    }

    fun setContentPosition(x : Float, y : Float) {
        _position.value = Position(x, y)
    }

    fun setTextSize(sp : Int) {
        _textSize.value = sp
    }

    fun getBookText() : String {
        return "${book.title}, ${book.author}"
    }

    fun setBackgroundImageUri(uri : String?) {
        _backgroundUri.value = uri
    }

    fun changeToTextEditMode() {
        _currentState.value = TodayOnelineWriteScreenState.TextEdit
    }

    fun handleBackPress() {
        when (_currentState.value) {
            TodayOnelineWriteScreenState.TextEdit -> {
                viewModelScope.launch {
                    _finishCall.emit(true)
                }
            }
            TodayOnelineWriteScreenState.TextMove -> {
                viewModelScope.launch {
                    _finishCall.emit(true)
                }
            }
            else -> { }
        }
    }

    fun handleNextPress() {
        println("state->> ${_currentState.value}")
        when (_currentState.value) {
            TodayOnelineWriteScreenState.TextEdit -> {
                _currentState.value = TodayOnelineWriteScreenState.TextMove
            }
            TodayOnelineWriteScreenState.TextMove -> {

            }
            else -> { }
        }
    }

    @AssistedFactory
    interface ViewModelAssistedFactory {
        fun create(book : BookItem) : WriteTodayOnelineWriteViewModel
    }

    companion object {
        fun providerViewModelFactory(
            assistedFactory: ViewModelAssistedFactory,
            book : BookItem
        ) : ViewModelProvider.Factory = object :  ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(book) as T
            }
        }
    }
}