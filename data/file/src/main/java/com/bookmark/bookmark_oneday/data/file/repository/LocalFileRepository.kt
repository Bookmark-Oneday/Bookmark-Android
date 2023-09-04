package com.bookmark.bookmark_oneday.data.file.repository

import android.content.Context
import android.os.Environment
import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.data.file.utils.isExternalStorageWritable
import com.bookmark.bookmark_oneday.domain.file.repository.FileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.coroutines.CoroutineContext

class LocalFileRepository(
    private val defaultDispatcher: CoroutineContext = Dispatchers.IO + SupervisorJob(),
    private val context : Context
) : FileRepository {
    override suspend fun uploadFile(file: File, fileName : String): BaseResponse<String> = withContext(defaultDispatcher) {
        try {
            if (!isExternalStorageWritable()) {
                return@withContext BaseResponse.Failure(
                    errorCode = 0,
                    errorMessage = "프로필 이미지를 저장할 수 없습니다.\n" +
                            "동일한 현상이 계속 발생하는 경우, 프로필 이미지를 제거하고 다시 시도해주세요."
                )
            }
            val targetFile : File
            withContext(Dispatchers.IO) {
                targetFile = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName)
                if (!targetFile.exists()) {
                    targetFile.createNewFile()
                }
                file.copyTo(targetFile, true)
            }

            return@withContext BaseResponse.Success(
                data = targetFile.toURI().toString()
            )

        } catch (e: Exception) {
            return@withContext BaseResponse.Failure(
                errorCode = e.hashCode(),
                errorMessage = "이미지를 업로드하는 과정에서 문제가 발생했습니다.\n" +
                        "동일한 현상이 계속 발생하는 경우, 프로필 이미지를 제거하고 다시 시도해주세요."
            )
        }
    }

}