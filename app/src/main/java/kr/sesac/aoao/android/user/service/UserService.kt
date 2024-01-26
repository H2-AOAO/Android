package kr.sesac.aoao.android.user.service

import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.user.model.response.*
import kr.sesac.aoao.android.user.model.request.*
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header

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
    @GET("/user")
    fun getProfile(
        @Header("authorization") accessToken: String
    ): Call<ApplicationResponse<ProfileResponse>>

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
}
