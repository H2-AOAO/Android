package kr.sesac.aoao.android.market.service

import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.market.model.request.UsePointRquest
import kr.sesac.aoao.android.market.model.response.MarketResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
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
    @POST("/dinos/point")
    fun usePoint(
        @Header("authorization") accessToken: String,
        @Body usePointRquest: UsePointRquest
    ): Call<ApplicationResponse<MarketResponse>>

}