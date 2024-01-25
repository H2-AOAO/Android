package kr.sesac.aoao.android.todofolder.service

import android.app.Activity
import kr.sesac.aoao.android.common.RetrofitConnection
import kr.sesac.aoao.android.common.RetrofitService
import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.todofolder.model.response.TodoQueryDetailResponse

/**
 * @since 2024.01.25
 * @author 김유빈
 */
object TodoRepository {

    private val todoService = RetrofitConnection.getInstance().create(TodoService::class.java)

    /**
     * 투두 리스트 조회 API 호출
     * @since 2024.01.25
     * @author 김유빈
     */
    fun findAll(
        accessToken: String,
        date: String,
        context: Activity,
        onResponse: (ApplicationResponse<TodoQueryDetailResponse>) -> Unit,
        onFailure: (Throwable) -> Unit,
    ) {
        RetrofitService.connect(
            todoService.findAll(accessToken, date),
            context, onResponse, onFailure
        )
    }
}
