package kr.sesac.aoao.android.user.service

import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.user.model.LoginRequest
import kr.sesac.aoao.android.user.model.Token
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("login") // 실제 엔드포인트에 맞게 수정
    fun login(@Body loginRequest: LoginRequest): Call<ApplicationResponse<Token>>
}