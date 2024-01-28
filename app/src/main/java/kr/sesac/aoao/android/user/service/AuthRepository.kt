package kr.sesac.aoao.android.user.service

import android.app.Activity
import kr.sesac.aoao.android.common.RetrofitConnection
import kr.sesac.aoao.android.common.RetrofitService
import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.common.model.ErrorResponse
import kr.sesac.aoao.android.user.model.request.DuplicatedEmailRequest
import kr.sesac.aoao.android.user.model.request.DuplicatedNicknameRequest
import kr.sesac.aoao.android.user.model.request.LoginRequest
import kr.sesac.aoao.android.user.model.request.SignupRequest
import kr.sesac.aoao.android.user.model.request.UserProfileUpdateResponse
import kr.sesac.aoao.android.user.model.response.SignupResponse
import kr.sesac.aoao.android.user.model.response.TokenResponse

/**
 * @since 2024.01.26
 * @author 이상민
 */
object AuthRepository {

    private val authService = RetrofitConnection.getInstance().create(AuthService::class.java)

    /**
     * 로그인 API 호출
     *
     * @return
     * @author 이상민
     * @since 2024.01.24
     */
    fun login(
        request: LoginRequest,
        context: Activity,
        onResponse: (ApplicationResponse<TokenResponse>) -> Unit,
        onFailure: (ErrorResponse) -> Unit,
    ) {
        RetrofitService.connect(
            authService.login(request),
            context, onResponse, onFailure
        )
    }


    /**
     * 화원가입 API
     *
     * @return
     * @author 이상민
     * @since 2024.01.25
     */
    fun signup(
        request: SignupRequest,
        context: Activity,
        onResponse: (ApplicationResponse<SignupResponse>) -> Unit,
        onFailure: (ErrorResponse) -> Unit,
    ) {
        RetrofitService.connect(
            authService.signup(request),
            context, onResponse, onFailure
        )
    }

    /**
     * 이메일 중복확인 API
     *
     * @return
     * @author 이상민
     * @since 2024.01.25
     */
    fun duplicationEmail(
        duplicatedEmailRequest: DuplicatedEmailRequest,
        context: Activity,
        onResponse: (ApplicationResponse<String>) -> Unit,
        onFailure: (ErrorResponse) -> Unit,
    ){
        RetrofitService.connect(
            authService.duplicationEmail(duplicatedEmailRequest),
            context,
            onResponse,
            onFailure
        )
    }

    /**
     * 닉네임 중복확인 API
     *
     * @return
     * @author 이상민
     * @since 2024.01.25
     */
    fun duplicationNickname(
        duplicatedNicknameRequest: DuplicatedNicknameRequest,
        context: Activity,
        onResponse: (ApplicationResponse<String>) -> Unit,
        onFailure: (ErrorResponse) -> Unit,
    ) {
        RetrofitService.connect(
            authService.duplicationNickname(duplicatedNicknameRequest),
            context, onResponse, onFailure
        )
    }


}
