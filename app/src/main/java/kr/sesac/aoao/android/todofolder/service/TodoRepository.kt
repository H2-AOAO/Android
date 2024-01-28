package kr.sesac.aoao.android.todofolder.service

import android.app.Activity
import kr.sesac.aoao.android.common.RetrofitConnection
import kr.sesac.aoao.android.common.RetrofitService
import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.common.model.ErrorResponse
import kr.sesac.aoao.android.model.TodoData
import kr.sesac.aoao.android.todofolder.model.request.TodoSaveRequest
import kr.sesac.aoao.android.todofolder.model.request.TodoUpdateRequest
import kr.sesac.aoao.android.todofolder.model.response.TodoQueryDetailResponse
import kr.sesac.aoao.android.todofolder.model.response.TodoSaveResponse

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
        onFailure: (ErrorResponse) -> Unit,
    ) {
        RetrofitService.connect(
            todoService.findAll(accessToken, date),
            context, onResponse, onFailure
        )
    }

    /**
     * 투두 생성 API 호출
     * @since 2024.01.25
     * @author 김유빈
     */
    fun save(
        accessToken: String,
        folderId: Long?,
        todo: TodoData,
        context: Activity,
        onResponse: (ApplicationResponse<TodoSaveResponse>) -> Unit,
        onFailure: (ErrorResponse) -> Unit,
    ) {
        RetrofitService.connect(
            todoService.save(accessToken, folderId, TodoSaveRequest(todo.content)),
            context, onResponse, onFailure
        )
    }

    /**
     * 투두 수정 API 호출
     * @since 2024.01.25
     * @author 김유빈
     */
    fun update(
        accessToken: String,
        folderId: Long?,
        todo: TodoData,
        context: Activity,
        onResponse: (ApplicationResponse<Void>) -> Unit,
        onFailure: (ErrorResponse) -> Unit,
    ) {
        RetrofitService.connect(
            todoService.update(accessToken, folderId, todo.id, TodoUpdateRequest(todo.content)),
            context, onResponse, onFailure
        )
    }

    /**
     * 투두 식제 API 호출
     * @since 2024.01.25
     * @author 김유빈
     */
    fun delete(
        accessToken: String,
        folderId: Long?,
        todoId: Long?,
        context: Activity,
        onResponse: (ApplicationResponse<Void>) -> Unit,
        onFailure: (ErrorResponse) -> Unit,
    ) {
        RetrofitService.connect(
            todoService.delete(accessToken, folderId, todoId),
            context, onResponse, onFailure
        )
    }

    /**
     * 투두 체크 API 호출
     * @since 2024.01.25
     * @author 김유빈
     */
    fun check(
        accessToken: String,
        folderId: Long?,
        todoId: Long?,
        context: Activity,
        onResponse: (ApplicationResponse<Void>) -> Unit,
        onFailure: (ErrorResponse) -> Unit,
    ) {
        RetrofitService.connect(
            todoService.check(accessToken, folderId, todoId),
            context, onResponse, onFailure
        )
    }

    /**
     * 투두 체크 취소 API 호출
     * @since 2024.01.25
     * @author 김유빈
     */
    fun uncheck(
        accessToken: String,
        folderId: Long?,
        todoId: Long?,
        context: Activity,
        onResponse: (ApplicationResponse<Void>) -> Unit,
        onFailure: (ErrorResponse) -> Unit,
    ) {
        RetrofitService.connect(
            todoService.uncheck(accessToken, folderId, todoId),
            context, onResponse, onFailure
        )
    }
}
