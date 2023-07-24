package com.bookmark.bookmark_oneday.domain.file.usecase

import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.file.repository.FileRepository
import java.io.File
import javax.inject.Inject

class UseCaseUploadFile @Inject constructor(
    private val repository : FileRepository
) {
    suspend operator fun invoke(file : File, fileName : String) : BaseResponse<String> = repository.uploadFile(file, fileName)
}