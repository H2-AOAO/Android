package kr.sesac.aoao.android.user.service

import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.user.model.response.*
import kr.sesac.aoao.android.user.model.request.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

/**
 * 토큰 이용 O
 *
* @since 2024.01.23
* @author 이상민
*/
interface UserService {

    /**
     * 유저 프로필 조회 API
     *
     * @return
     * @author 이상민
     * @since 2024.01.25
     */
    @GET("/users/{userId}")
    fun getMypage(
        @Header("authorization") accessToken: String,
        @Path("userId") userId: Long
    ): Call<ApplicationResponse<MypageResponse>>

    /**
     * 유저 삭제 API
     *
     * @return
     * @author 이상민
     * @since 2024.01.25
     */
    @DELETE("/user/delete")
    fun deleteUser(
        @Header("authorization") accessToken: String
    ): Call<ApplicationResponse<String>>

    /**
     * 닉네임 수정 API
     *
     * @return
     * @author 이상민
     * @since 2024.01.28
     */
    @POST("/users/nickname")
    fun updateNickname(
        @Header("authorization") accessToken: String,
        @Body request: UserNicknameUpdateRequest
    ): Call<ApplicationResponse<String>>

    /**
     * 비밀번호 수정 API
     *
     * @return
     * @author 이상민
     * @since 2024.01.28
     */
    @POST("/users/password")
    fun updatePassword(
        @Header("authorization") accessToken: String,
        @Body request: UserPasswordUpdateRequest
    ): Call<ApplicationResponse<String>>

    /**
     * 프로필 수정 API
     *
     * @return
     * @author 이상민
     * @since 2024.01.28
     */
    @Multipart
    @POST("/users/profile")
    fun updateProfile(
        @Header("authorization") accessToken: String,
        @Part image: MultipartBody.Part
    ): Call<ApplicationResponse<UserProfileUpdateResponse>>

    /**
     * 로그아웃 API
     *
     * @return
     * @author 이상민
     * @since 2024.01.28
     */
    @POST("/users/logout")
    fun logout(
        @Header("authorization") accessToken: String
    ): Call<ApplicationResponse<String>>

}
