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

interface DinoInfoService {
    @GET("/dinos/{userId}")
    fun getDinoInfo(@Path("userId") userId: Long): Call<ApplicationResponse<DinoResponse>>

    @FormUrlEncoded
    @POST("/items/num")
    fun getItemInfo(
        @Field("userId") userId: Long,
        @Field("itemId") itemId: Int,
        @Field("status") status: String
    ): Call<ApplicationResponse<UserItemResponse>>
}