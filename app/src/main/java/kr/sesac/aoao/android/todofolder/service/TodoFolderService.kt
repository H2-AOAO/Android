package kr.sesac.aoao.android.todofolder.service

import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.todofolder.model.request.FolderSaveRequest
import kr.sesac.aoao.android.todofolder.model.request.FolderUpdateRequest
import kr.sesac.aoao.android.todofolder.model.response.FolderQueryDetailResponse
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
interface TodoFolderService {

    /**
     * 폴더리스트 목록 조회
     * @since 2024.01.24
     * @parameter String
     * @return ApplicationResponse<FolderQueryDetailResponse>
     * @author 김유빈
     */
    @GET("/folders?date={date}")
    fun findAll(
        @Path("date") date: String
    ) : Call<ApplicationResponse<FolderQueryDetailResponse>>

    /**
     * 폴더 생성
     * @since 2024.01.24
     * @parameter FolderSaveRequest
     * @return ApplicationResponse<Void>
     * @author 김유빈
     */
    @POST("/folders")
    fun save(
        @Body folder: FolderSaveRequest
    ) : Call<ApplicationResponse<Void>>

    /**
     * 폴더 수정
     * @since 2024.01.24
     * @parameter Long, FolderUpdateRequest
     * @return ApplicationResponse<Void>
     * @author 김유빈
     */
    @POST("/folders/{folderId}")
    fun update(
        @Path("folderId") folderId: Long,
        @Body folder: FolderUpdateRequest
    ) : Call<ApplicationResponse<Void>>

    /**
     * 폴더 삭제
     * @since 2024.01.24
     * @parameter Long
     * @return ApplicationResponse<Void>
     * @author 김유빈
     */
    @DELETE("/folders/{folderId}")
    fun delete(
        @Path("folderId") folderId: Long
    ) : Call<ApplicationResponse<Void>>
}
