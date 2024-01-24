package kr.sesac.aoao.android.market.service

import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.market.model.MarketResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MarketService {
    @FormUrlEncoded
    @POST("/dinos/point")
    fun usePoint(
        @Field("userId") userId: Long,
        @Field("itemId") itemId: Long,
    ): Call<ApplicationResponse<MarketResponse>>
}