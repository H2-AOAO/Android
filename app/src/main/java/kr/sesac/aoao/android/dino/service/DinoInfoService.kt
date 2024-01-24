package kr.sesac.aoao.android.dino.service

import kr.sesac.aoao.android.common.model.ApplicationResponse
import retrofit2.Call
import kr.sesac.aoao.android.dino.model.DinoResponse
import kr.sesac.aoao.android.dino.model.UserItemResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
/**
 * @since 2024.01.18
 * @author 김은서
 */
interface DinoInfoService {

    /**
     * 다이노 정보 조회
     * @since 2024.01.23
     * @return ApplicationResponse<DinoResponse>
     * @author 김은서
     */
    @GET("/dinos/{userId}")
    fun getDinoInfo(@Path("userId") userId: Long): Call<ApplicationResponse<DinoResponse>>

    /**
     * 아이템 개수 조절
     * @since 2024.01.23
     * @return ApplicationResponse<UserItemResponse>
     * @author 김은서
     */
    @FormUrlEncoded
    @POST("/items/num")
    fun getItemInfo(
        @Field("userId") userId: Long,
        @Field("itemId") itemId: Int,
        @Field("status") status: String
    ): Call<ApplicationResponse<UserItemResponse>>

    /**
     * 경험치 조절
     * @since 2024.01.23
     * @return ApplicationResponse<DinoResponse>
     * @author 김은서
     */
    @FormUrlEncoded
    @POST("/dinos/exp")
    fun UpEXP(
        @Field("userId") userId: Long,
        @Field("currLv") currLv: Int,
        @Field("currExp") currExp : Int
    ): Call<ApplicationResponse<DinoResponse>>
}