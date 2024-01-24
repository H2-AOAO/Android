package kr.sesac.aoao.android.market.service

import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.market.model.MarketResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
/**
 * @since 2024.01.18
 * @author 김은서
 */
interface MarketService {
    /**
     * 아이템 구매 포인트 변경
     * @since 2024.01.23
     * @author 김은서
     */
    @FormUrlEncoded
    @POST("/dinos/point")
    fun usePoint(
        @Field("userId") userId: Long,
        @Field("itemId") itemId: Long,
    ): Call<ApplicationResponse<MarketResponse>>
}