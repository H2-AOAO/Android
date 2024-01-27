package kr.sesac.aoao.android.calendar.diary.service

import kr.sesac.aoao.android.calendar.diary.model.request.DiarySaveRequest
import kr.sesac.aoao.android.calendar.diary.model.response.DiaryResponse
import kr.sesac.aoao.android.common.model.ApplicationResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * @since 2024.01.28
 * @author 김유빈
 */
interface DiaryService {

    /**
     * 다이어리 조회
     * @since 2024.01.28
     * @parameter String, String?
     * @return ApplicationResponse<DiaryResponse>
     * @author 김유빈
     */
    @GET("/diary")
    fun findByDate(
        @Header("authorization") accessToken: String,
        @Query("date") date: String?,
    ) : Call<ApplicationResponse<DiaryResponse>>

    /**
     * 다이어리 저장
     * @since 2024.01.28
     * @parameter String, DiarySaveRequest
     * @return ApplicationResponse<DiarySaveRequest>
     * @author 김유빈
     */
    @POST("/diary")
    fun save(
        @Header("authorization") accessToken: String,
        @Body data: DiarySaveRequest,
    ) : Call<ApplicationResponse<String>>
}
