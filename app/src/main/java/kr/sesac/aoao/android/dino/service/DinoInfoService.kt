package kr.sesac.aoao.android.dino.service

import kr.sesac.aoao.android.common.model.ApplicationResponse
import retrofit2.Call
import kr.sesac.aoao.android.dino.model.DinoResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DinoInfoService {
    @GET("/dinos/{userId}")
    fun getDinoInfo(@Path("userId") userId: Long): Call<ApplicationResponse<DinoResponse>>
}