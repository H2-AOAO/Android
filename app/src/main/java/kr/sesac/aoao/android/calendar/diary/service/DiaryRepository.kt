package kr.sesac.aoao.android.calendar.diary.service

import android.app.Activity
import kr.sesac.aoao.android.calendar.diary.model.request.DiarySaveRequest
import kr.sesac.aoao.android.calendar.diary.model.request.DiaryUpdateRequest
import kr.sesac.aoao.android.calendar.diary.model.response.DiaryResponse
import kr.sesac.aoao.android.common.RetrofitConnection
import kr.sesac.aoao.android.common.RetrofitService
import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.common.model.ErrorResponse

/**
 * @since 2024.01.28
 * @author 김유빈
 */
object DiaryRepository {

    private val diaryService = RetrofitConnection.getInstance().create(DiaryService::class.java)

    /**
     * 다이어리 조회 API 호출
     * @since 2024.01.28
     * @author 김유빈
     */
    fun findByDate(
        accessToken: String,
        date: String,
        context: Activity,
        onResponse: (ApplicationResponse<DiaryResponse>) -> Unit,
        onFailure: (ErrorResponse) -> Unit,
    ) {
        RetrofitService.connect(
            diaryService.findByDate(accessToken, date),
            context, onResponse, onFailure
        )
    }

    /**
     * 다이어리 저장 API 호출
     * @since 2024.01.28
     * @author 김유빈
     */
    fun save(
        accessToken: String,
        date: String,
        content: String,
        context: Activity,
        onResponse: (ApplicationResponse<String>) -> Unit,
        onFailure: (ErrorResponse) -> Unit,
    ) {
        RetrofitService.connect(
            diaryService.save(accessToken, DiarySaveRequest(date, content)),
            context, onResponse, onFailure
        )
    }

    /**
     * 다이어리 수정 API 호출
     * @since 2024.01.28
     * @author 김유빈
     */
    fun update(
        accessToken: String,
        diaryId: Long?,
        content: String,
        context: Activity,
        onResponse: (ApplicationResponse<String>) -> Unit,
        onFailure: (ErrorResponse) -> Unit,
    ) {
        RetrofitService.connect(
            diaryService.update(accessToken, diaryId, DiaryUpdateRequest(content)),
            context, onResponse, onFailure
        )
    }

    /**
     * 다이어리 삭제 API 호출
     * @since 2024.01.28
     * @author 김유빈
     */
    fun delete(
        accessToken: String,
        diaryId: Long?,
        context: Activity,
        onResponse: (ApplicationResponse<String>) -> Unit,
        onFailure: (ErrorResponse) -> Unit,
    ) {
        RetrofitService.connect(
            diaryService.delete(accessToken, diaryId),
            context, onResponse, onFailure
        )
    }
}
