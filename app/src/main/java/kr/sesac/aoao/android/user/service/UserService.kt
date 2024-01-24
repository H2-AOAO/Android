package kr.sesac.aoao.android.user.service

import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.user.model.LoginRequest
import kr.sesac.aoao.android.user.model.ProfileResponse
import kr.sesac.aoao.android.user.model.TokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserService {

    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<ApplicationResponse<TokenResponse>>

    @GET("/user") // 실제 엔드포인트에 맞게 수정
    fun getProfile(
        @Header("authorization") accessToken: String
    ): Call<ApplicationResponse<ProfileResponse>>
}