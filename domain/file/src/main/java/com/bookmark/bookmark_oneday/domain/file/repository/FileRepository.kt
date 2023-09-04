package com.bookmark.bookmark_oneday.domain.file.repository

import com.bookmark.bookmark_oneday.core.model.BaseResponse
import java.io.File

interface FileRepository {
    suspend fun uploadFile(file : File, fileName : String) : BaseResponse<String>
}