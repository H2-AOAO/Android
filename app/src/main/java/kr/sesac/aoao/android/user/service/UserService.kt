package kr.sesac.aoao.android.user.service

import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.user.model.response.*
import kr.sesac.aoao.android.user.model.request.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

/**
* @since 2024.01.21
* @author 이상민
*/
interface UserService {

    @POST("login")
    fun login(
        @Body loginRequest: LoginRequest
    ): Call<ApplicationResponse<TokenResponse>>

    /**
     * 화원가입
     *
     * @return
     * @author 이상민
     * @since 2024.01.25
     */
    @POST("/signup")
    fun signup(
        @Body signupRequest: SignupRequest
    ): Call<ApplicationResponse<SignupResponse>>

    @POST("/duplicated/email")
    fun duplicationEmail(
        @Body duplicatedNicknameRequest: DuplicatedEmailRequest
    ): Call<ApplicationResponse<String>>

    @POST("/duplicated/nickname")
    fun duplicationNickname(
        @Body duplicatedNicknameRequest: DuplicatedNicknameRequest
    ): Call<ApplicationResponse<String>>

    @GET("/user")
    fun getProfile(
        @Header("authorization") accessToken: String
    ): Call<ApplicationResponse<ProfileResponse>>

    @DELETE("/user/delete")
    fun deleteUser(
        @Header("authorization") accessToken: String
    ): Call<ApplicationResponse<String>>
}
