package kr.sesac.aoao.android.user.service

import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.user.model.request.DuplicatedEmailRequest
import kr.sesac.aoao.android.user.model.request.DuplicatedNicknameRequest
import kr.sesac.aoao.android.user.model.request.LoginRequest
import kr.sesac.aoao.android.user.model.request.SignupRequest
import kr.sesac.aoao.android.user.model.response.AccessToken
import kr.sesac.aoao.android.user.model.response.SignupResponse
import kr.sesac.aoao.android.user.model.response.TokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * 토큰 이용 X
 *
 * @since 2024.01.25
 * @author 이상민
 */
interface AuthService {

    /**
     * 로그인 API
     *
     * @return
     * @author 이상민
     * @since 2024.01.24
     */
    @POST("/login/kakao")
    fun loginKakao(
        @Body accessTokenDto: AccessToken
    ): Call<ApplicationResponse<TokenResponse>>

    /**
     * 로그인 API
     *
     * @return
     * @author 이상민
     * @since 2024.01.24
     */
    @POST("login")
    fun login(
        @Body loginRequest: LoginRequest
    ): Call<ApplicationResponse<TokenResponse>>

    /**
     * 화원가입 API
     *
     * @return
     * @author 이상민
     * @since 2024.01.25
     */
    @POST("/signup")
    fun signup(
        @Body signupRequest: SignupRequest
    ): Call<ApplicationResponse<SignupResponse>>

    /**
     * 이메일 중복확인 API
     *
     * @return
     * @author 이상민
     * @since 2024.01.25
     */
    @POST("/duplicated/email")
    fun duplicationEmail(
        @Body duplicatedNicknameRequest: DuplicatedEmailRequest
    ): Call<ApplicationResponse<String>>

    /**
     * 닉네임 중복확인 API
     *
     * @return
     * @author 이상민
     * @since 2024.01.25
     */
    @POST("/duplicated/nickname")
    fun duplicationNickname(
        @Body duplicatedNicknameRequest: DuplicatedNicknameRequest
    ): Call<ApplicationResponse<String>>
}
