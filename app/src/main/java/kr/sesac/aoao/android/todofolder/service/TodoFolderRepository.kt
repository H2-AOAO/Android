package kr.sesac.aoao.android.todofolder.service

import android.app.Activity
import kr.sesac.aoao.android.common.RetrofitConnection
import kr.sesac.aoao.android.common.RetrofitService
import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.model.TodoFolderData
import kr.sesac.aoao.android.todofolder.model.request.FolderSaveRequest
import kr.sesac.aoao.android.todofolder.model.request.FolderUpdateRequest
import kr.sesac.aoao.android.todofolder.model.response.FolderQueryDetailResponse

/**
 * @since 2024.01.25
 * @author 김유빈
 */
object TodoFolderRepository {

    private val folderService = RetrofitConnection.getInstance().create(TodoFolderService::class.java)

    /**
     * 폴더 리스트 조회 API 호출
     * @since 2024.01.25
     * @author 김유빈
     */
    fun findAll(
        accessToken: String,
        date: String?,
        context: Activity,
        onResponse: (ApplicationResponse<FolderQueryDetailResponse>) -> Unit,
        onFailure: (Throwable) -> Unit,
    ) {
        RetrofitService.connect(
            folderService.findAll(accessToken, date),
            context, onResponse, onFailure
        )
    }

    /**
     * 폴더 생성 API 호출
     * @since 2024.01.25
     * @author 김유빈
     */
    fun save(
        accessToken: String,
        folder: TodoFolderData,
        date: String,
        paletteId: Long?,
        context: Activity,
        onResponse: (ApplicationResponse<Void>) -> Unit,
        onFailure: (Throwable) -> Unit,
    ) {
        RetrofitService.connect(
            folderService.save(accessToken, FolderSaveRequest(folder.name, date, paletteId)),
            context, onResponse, onFailure
        )
    }

    /**
     * 폴더 수정 API 호출
     * @since 2024.01.25
     * @author 김유빈
     */
    fun update(
        accessToken: String,
        folderId: Long?,
        name: String,
        paletteId: Long?,
        context: Activity,
        onResponse: (ApplicationResponse<Void>) -> Unit,
        onFailure: (Throwable) -> Unit,
    ) {
        RetrofitService.connect(
            folderService.update(accessToken, folderId, FolderUpdateRequest(name, paletteId)),
                context, onResponse, onFailure
        )
    }

    /**
     * 폴더 삭제 API 호출
     * @since 2024.01.25
     * @author 김유빈
     */
    fun delete(
        accessToken: String,
        folderId: Long?,
        context: Activity,
        onResponse: (ApplicationResponse<Void>) -> Unit,
        onFailure: (Throwable) -> Unit,
    ) {
        RetrofitService.connect(
            folderService.delete(accessToken, folderId),
            context, onResponse, onFailure
        )
    }
}
