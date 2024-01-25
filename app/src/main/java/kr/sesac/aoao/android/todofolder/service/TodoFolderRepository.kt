package kr.sesac.aoao.android.todofolder.service

import android.app.Activity
import kr.sesac.aoao.android.common.RetrofitConnection
import kr.sesac.aoao.android.common.RetrofitService
import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.todofolder.model.response.FolderQueryDetailResponse

object TodoFolderRepository {

    private val folderService = RetrofitConnection.getInstance().create(TodoFolderService::class.java)

    fun findAll(
        accessToken: String,
        date: String,
        context: Activity,
        onResponse: (ApplicationResponse<FolderQueryDetailResponse>) -> Unit,
        onFailure: (Throwable) -> Unit,
    ) {
        RetrofitService.connect(
            folderService.findAll(accessToken, date),
            context,
            onResponse, onFailure
        )
    }
}
