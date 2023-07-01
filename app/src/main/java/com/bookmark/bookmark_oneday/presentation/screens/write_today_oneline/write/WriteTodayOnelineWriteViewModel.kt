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
import com.bookmark.bookmark_oneday.presentation.util.DarkModeChecker
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
    darkModeChecker: DarkModeChecker,
    @Assisted val book : BookItem
) : ViewModel() {

    private val _finishWithSuccess = MutableSharedFlow<Boolean>()
    val finishWithSuccess = _finishWithSuccess.asSharedFlow()

    private val _content = MutableStateFlow("")
    val content = _content.asStateFlow()

    private val _currentState = MutableStateFlow<TodayOnelineWriteScreenState>(TodayOnelineWriteScreenState.TextMove)
    val currentState = _currentState.asStateFlow()

    private val _textColor = MutableStateFlow(if (darkModeChecker.isDarkMode()) "#FFFFFF" else "#000000")
    val textColor = _textColor.asStateFlow()

    private val _backgroundUri = MutableStateFlow<String?>(null)
    val backgroundUri = _backgroundUri.asStateFlow()

    private val _textSize = MutableStateFlow(18)
    val textSize = _textSize.asStateFlow()

    private val _position = MutableStateFlow(Position(0.5f, 0.5f))
    val position = _position.asStateFlow()

    private val _font = MutableStateFlow(Font.defaultList[0])
    val font = _font.asStateFlow()

    private val _bottomViewTranslationY = MutableStateFlow(0f)
    val bottomViewTranslationY = _bottomViewTranslationY.asStateFlow()

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
        _currentState.value = TodayOnelineWriteScreenState.TextEdit(EditTextDetailState.IME)
    }

    fun handleBackPress() {
        when (_currentState.value) {
            is TodayOnelineWriteScreenState.TextEdit -> {
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
            is TodayOnelineWriteScreenState.TextEdit -> {
                _currentState.value = TodayOnelineWriteScreenState.TextMove
            }
            TodayOnelineWriteScreenState.TextMove -> {
                val oneLineContent = OneLineContent(
                    id = book.id,
                    bookTitle = book.title,
                    authors = book.author.split(","),
                    oneliner = _content.value,
                    textColor = _textColor.value,
                    textSize = _textSize.value.toString(),
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
        val currentState = _currentState.value
        if (currentState !is TodayOnelineWriteScreenState.TextEdit) return

        val detailState = currentState.editTextDetailState
        if (detailState is EditTextDetailState.Font && state is EditTextDetailState.Font) {
            _currentState.value = TodayOnelineWriteScreenState.TextEdit(EditTextDetailState.IME)
        } else {
            _currentState.value = TodayOnelineWriteScreenState.TextEdit(state)
        }
    }

    fun setBottomViewTranslationY(y : Float) {
        _bottomViewTranslationY.value = y
    }

    fun closeIme() {
        val state = _currentState.value
        if (state !is TodayOnelineWriteScreenState.TextEdit) return

        if (state.editTextDetailState == EditTextDetailState.IME) {
            _currentState.value = TodayOnelineWriteScreenState.TextEdit(EditTextDetailState.Normal)
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