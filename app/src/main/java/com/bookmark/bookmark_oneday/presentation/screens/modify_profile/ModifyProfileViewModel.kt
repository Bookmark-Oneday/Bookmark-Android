package com.bookmark.bookmark_oneday.presentation.screens.modify_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.domain.usecase.UseCaseGetUser
import com.bookmark.bookmark_oneday.domain.usecase.UseCaseSetUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModifyProfileViewModel @Inject constructor(
    useCaseGetUser: UseCaseGetUser,
    private val useCaseSetUser: UseCaseSetUser
): ViewModel() {
    private val userInfo = useCaseGetUser.getProfile()

    private val _bio = MutableStateFlow("")
    private val _nickname = MutableStateFlow("")
    private val _profileImageUri = MutableStateFlow<String?>(null)

    val bio = _bio.asStateFlow()
    val nickname = _nickname.asStateFlow()
    val profileImageUri = _profileImageUri.asStateFlow()

    private val _modifyProfileResult = MutableSharedFlow<Boolean>()
    val modifyProfileResult = _modifyProfileResult.asSharedFlow()

    init {
        viewModelScope.launch {
            userInfo.collectLatest {
                _bio.value = it.bio
                _nickname.value = it.nickname
                _profileImageUri.value = it.profileImage
            }
        }
    }
    fun setProfileImageUri(imageUri : String?) {
        _profileImageUri.value = imageUri
    }

    fun setBio(inputBio : String) {
        _bio.value = inputBio
    }

    fun tryModifyUserInfo() {
        viewModelScope.launch {
            useCaseSetUser.setUserProfile(nickname = _nickname.value, bio = _bio.value, profileUri = _profileImageUri.value)
            _modifyProfileResult.emit(true)
        }
    }
}