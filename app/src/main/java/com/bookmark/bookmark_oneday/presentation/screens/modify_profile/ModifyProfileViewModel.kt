package com.bookmark.bookmark_oneday.presentation.screens.modify_profile

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.file.usecase.UseCaseUploadFile
import com.bookmark.bookmark_oneday.domain.user.usecase.UseCaseGetUser
import com.bookmark.bookmark_oneday.domain.user.usecase.UseCaseSetUser
import com.bookmark.bookmark_oneday.presentation.util.FileMapper
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
    private val useCaseSetUser: UseCaseSetUser,
    private val useCaseUploadFile: UseCaseUploadFile,
    private val fileMapper: FileMapper
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

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

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
            val currentProfileImageUri = profileImageUri.value
            val profileImagePath = if (currentProfileImageUri != null) {
                val file = fileMapper.uriToImageFile(currentProfileImageUri.toUri(), "temp_profile.jpg")
                val response = useCaseUploadFile(file, "profile.jpg")

                if (response is BaseResponse.Failure) {
                    _toastMessage.emit(response.errorMessage)
                    return@launch
                }

                (response as BaseResponse.Success<String>).data
            } else {
                null
            }

            useCaseSetUser.setUserProfile(nickname = _nickname.value, bio = _bio.value, profileUri = profileImagePath)
            _modifyProfileResult.emit(true)
        }
    }
}