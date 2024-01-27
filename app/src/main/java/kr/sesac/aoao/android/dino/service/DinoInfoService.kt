package kr.sesac.aoao.android.dino.service

import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.dino.model.request.ExpRequset
import kr.sesac.aoao.android.dino.model.request.ItemNumRequset
import kr.sesac.aoao.android.dino.model.request.NewDinoRequest
import retrofit2.Call
import kr.sesac.aoao.android.dino.model.response.DinoResponse
import kr.sesac.aoao.android.dino.model.response.UserItemResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

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
    @GET("/dinos/")
    fun getDinoInfo(
        @Header("authorization") accessToken: String): Call<ApplicationResponse<DinoResponse>>

    /**
     * 아이템 개수 조절
     * @since 2024.01.23
     * @return ApplicationResponse<UserItemResponse>
     * @author 김은서
     */
    @POST("/items/num")
    fun getItemInfo(
        @Header("authorization") accessToken: String,
        @Body useItem : ItemNumRequset
    ): Call<ApplicationResponse<UserItemResponse>>

    /**
     * 경험치 조절
     * @since 2024.01.23
     * @return ApplicationResponse<DinoResponse>
     * @author 김은서
     */
    @POST("/dinos/exp")
    fun UpEXP(
        @Header("authorization") accessToken: String,
        @Body expUp : ExpRequset
    ): Call<ApplicationResponse<DinoResponse>>

    /**
     * 새로운 다이노 추가
     * @since 2024.01.26
     * @return ApplicationResponse<Boolean>
     * @author 김은서
     */
    @POST("/dinos/newdino")
    fun newDino(
        @Header("authorization") accessToken: String,
        @Body newDino : NewDinoRequest
    ): Call<ApplicationResponse<Boolean>>
}