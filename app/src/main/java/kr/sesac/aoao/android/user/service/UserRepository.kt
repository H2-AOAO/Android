package kr.sesac.aoao.android.user.service

import android.app.Activity
import kr.sesac.aoao.android.common.RetrofitConnection
import kr.sesac.aoao.android.common.RetrofitService
import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.common.model.ErrorResponse
import kr.sesac.aoao.android.user.model.request.UserNicknameUpdateRequest
import kr.sesac.aoao.android.user.model.request.UserPasswordUpdateRequest
import kr.sesac.aoao.android.user.model.request.UserProfileUpdateResponse
import kr.sesac.aoao.android.user.model.response.MypageResponse
import okhttp3.MultipartBody

object UserRepository {

    private val userService = RetrofitConnection.getInstance().create(UserService::class.java)

    /**
     * 유저 프로필 조회 API 호출
     *
     * @return
     * @author 이상민
     * @since 2024.01.27
     */
    fun getMypage(
        accessToken: String,
        userId: Long,
        context: Activity,
        onResponse: (ApplicationResponse<MypageResponse>) -> Unit,
        onFailure: (ErrorResponse) -> Unit,
    ) {
        RetrofitService.connect(
            userService.getMypage(accessToken, userId),
            context, onResponse, onFailure
        )
    }

    /**
     * 닉네임 수정 API 호출
     *
     * @return
     * @author 이상민
     * @since 2024.01.27
     */
    fun updateNickname(
        accessToken: String,
        request: UserNicknameUpdateRequest,
        context: Activity,
        onResponse: (ApplicationResponse<String>) -> Unit,
        onFailure: (ErrorResponse) -> Unit,
    ) {
        RetrofitService.connect(
            userService.updateNickname(accessToken, request),
            context, onResponse, onFailure
        )
    }

    /**
     * 비밀번호 수정 API 호출
     *
     * @return
     * @author 이상민
     * @since 2024.01.27
     */
    fun updatePassword(
        accessToken: String,
        request: UserPasswordUpdateRequest,
        context: Activity,
        onResponse: (ApplicationResponse<String>) -> Unit,
        onFailure: (ErrorResponse) -> Unit,
    ) {
        RetrofitService.connect(
            userService.updatePassword(accessToken, request),
            context, onResponse, onFailure
        )
    }

    /**
     * 프로필 수정 API 호출
     *
     * @return
     * @author 이상민
     * @since 2024.01.27
     */
    fun updateProfile(
        accessToken: String,
        image: MultipartBody.Part,
        context: Activity,
        onResponse: (ApplicationResponse<UserProfileUpdateResponse>) -> Unit,
        onFailure: (ErrorResponse) -> Unit,
    ) {
        RetrofitService.connect(
            userService.updateProfile(accessToken, image),
            context, onResponse, onFailure
        )
    }

    /**
     * 로그아웃 API 호출
     *
     * @return
     * @author 이상민
     * @since 2024.01.28
     */
    fun logout(
        accessToken: String,
        context: Activity,
        onResponse: (ApplicationResponse<String>) -> Unit,
        onFailure: (ErrorResponse) -> Unit,
    ) {
        RetrofitService.connect(
            userService.logout(accessToken),
            context, onResponse, onFailure
        )
    }
}
