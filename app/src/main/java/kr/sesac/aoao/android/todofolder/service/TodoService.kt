package kr.sesac.aoao.android.todofolder.service

import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.todofolder.model.request.TodoSaveRequest
import kr.sesac.aoao.android.todofolder.model.request.TodoUpdateRequest
import kr.sesac.aoao.android.todofolder.model.response.TodoQueryDetailResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * @since 2024.01.24
 * @author 김유빈
 */
interface TodoService {

    /**
     * 투두리스트 목록 조회
     * @since 2024.01.24
     * @parameter String
     * @return ApplicationResponse<TodoQueryDetailResponse>
     * @author 김유빈
     */
    @GET("/todos?date={date}")
    fun findAll(
        @Path("date") date: String
    ) : Call<ApplicationResponse<TodoQueryDetailResponse>>

    /**
     * 투두 생성
     * @since 2024.01.24
     * @parameter Long, TodoSaveRequest
     * @return ApplicationResponse<Void>
     * @author 김유빈
     */
    @POST("/folders/{folderId}/todos")
    fun save(
        @Path("folderId") folderId: Long,
        @Body todo: TodoSaveRequest
    ) : Call<ApplicationResponse<Void>>

    /**
     * 투두 수정
     * @since 2024.01.24
     * @parameter Long, Long, TodoUpdateRequest
     * @return ApplicationResponse<Void>
     * @author 김유빈
     */
    @POST("/folders/{folderId}/todos/{todoId}")
    fun update(
        @Path("folderId") folderId: Long,
        @Path("todoId") todoId: Long,
        @Body todo: TodoUpdateRequest
    ) : Call<ApplicationResponse<Void>>

    /**
     * 투두 삭제
     * @since 2024.01.24
     * @parameter Long, Long
     * @return ApplicationResponse<Void>
     * @author 김유빈
     */
    @DELETE("/folders/{folderId}/todos/{todoId}")
    fun delete(
        @Path("folderId") folderId: Long,
        @Path("todoId") todoId: Long
    ) : Call<ApplicationResponse<Void>>

    /**
     * 투두 체크
     * @since 2024.01.24
     * @parameter Long, Long
     * @return ApplicationResponse<Void>
     * @author 김유빈
     */
    @POST("/folders/{folderId}/todos/{todoId}/check")
    fun check(
        @Path("folderId") folderId: Long,
        @Path("todoId") todoId: Long
    ) : Call<ApplicationResponse<Void>>

    /**
     * 투두 체크 취소
     * @since 2024.01.24
     * @parameter Long, Long
     * @return ApplicationResponse<Void>
     * @author 김유빈
     */
    @POST("/folders/{folderId}/todos/{todoId}/uncheck")
    fun uncheck(
        @Path("folderId") folderId: Long,
        @Path("todoId") todoId: Long
    ) : Call<ApplicationResponse<Void>>
}
