package com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.write

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.BookItem
import com.bookmark.bookmark_oneday.domain.model.OneLineContent
import com.bookmark.bookmark_oneday.domain.usecase.UseCaseRegisterOneLine
import com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.write.model.EditTextDetailState
import com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.write.model.Font
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
    private val useCaseRegisterOneLine: UseCaseRegisterOneLine,
    @Assisted val book : BookItem
) : ViewModel() {

    private val _finishWithSuccess = MutableSharedFlow<Boolean>()
    val finishWithSuccess = _finishWithSuccess.asSharedFlow()

    private val _content = MutableStateFlow("")
    val content = _content.asStateFlow()

    private val _currentState = MutableStateFlow<TodayOnelineWriteScreenState>(TodayOnelineWriteScreenState.TextMove)
    val currentState = _currentState.asStateFlow()

    private val _editTextDetailState = MutableStateFlow<EditTextDetailState>(EditTextDetailState.Normal)
    val editTextDetailState = _editTextDetailState.asStateFlow()

    private val _textColor = MutableStateFlow("#000000")
    val textColor = _textColor.asStateFlow()

    private val _backgroundUri = MutableStateFlow<String?>(null)
    val backgroundUri = _backgroundUri.asStateFlow()

    private val _textSize = MutableStateFlow(18)
    val textSize = _textSize.asStateFlow()

    private val _position = MutableStateFlow(Position(0.5f, 0.5f))
    val position = _position.asStateFlow()

    private val _font = MutableStateFlow(Font.defaultList[0])
    val font = _font.asStateFlow()

    var imeHeight = 0

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

    fun setFont(font : Font) {
        _font.value = font
    }

    fun changeToTextEditMode() {
        _currentState.value = TodayOnelineWriteScreenState.TextEdit
        _editTextDetailState.value = EditTextDetailState.Normal
    }

    fun handleBackPress() {
        when (_currentState.value) {
            TodayOnelineWriteScreenState.TextEdit -> {
                viewModelScope.launch {
                    _finishWithSuccess.emit(false)
                }
            }
            TodayOnelineWriteScreenState.TextMove -> {
                viewModelScope.launch {
                    _finishWithSuccess.emit(false)
                }
            }
            else -> {
                // 업로드중인 경우, 뒤로가기 버튼을 눌러도 아무것도 하지 않습니다.
            }
        }
    }

    fun handleNextPress() {
        when (_currentState.value) {
            TodayOnelineWriteScreenState.TextEdit -> {
                _currentState.value = TodayOnelineWriteScreenState.TextMove
                _editTextDetailState.value = EditTextDetailState.Normal
            }
            TodayOnelineWriteScreenState.TextMove -> {
                val oneLineContent = OneLineContent(
                    id = book.id,
                    bookTitle = book.title,
                    authors = book.author.split(","),
                    oneliner = _content.value,
                    textColor = _textColor.value,
                    textSize = _textSize.toString(),
                    centerX = _position.value.x.toString(),
                    centerY = _position.value.y.toString(),
                    font = _font.value.title,
                    backgroundImageUri = _backgroundUri.value
                )
                viewModelScope.launch {
                    _currentState.value = TodayOnelineWriteScreenState.Uploading
                    val response = useCaseRegisterOneLine(oneLineContent)

                    if (response is BaseResponse.EmptySuccess) {
                        _finishWithSuccess.emit(true)
                    } else {
                        _currentState.value = TodayOnelineWriteScreenState.TextMove
                    }
                }
            }
            else -> {}
        }
    }

    fun setEditTextDetailState(state : EditTextDetailState) {
        if (_currentState.value !is TodayOnelineWriteScreenState.TextEdit) return

        if (_editTextDetailState.value is EditTextDetailState.Font && state is EditTextDetailState.Font) {
            _editTextDetailState.value = EditTextDetailState.IME
        } else {
            _editTextDetailState.value = state
        }
    }

    fun hideSoftKeyboard() {
        if (_currentState.value !is TodayOnelineWriteScreenState.TextEdit) return

        if (_editTextDetailState.value is EditTextDetailState.IME) {
            _editTextDetailState.value = EditTextDetailState.Normal
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