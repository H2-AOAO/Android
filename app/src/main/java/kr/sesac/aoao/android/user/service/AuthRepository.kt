package kr.sesac.aoao.android.user.service

import android.app.Activity
import kr.sesac.aoao.android.common.RetrofitConnection
import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.user.model.request.DuplicatedEmailRequest

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


    /**
     * 화원가입 API
     *
     * @return
     * @author 이상민
     * @since 2024.01.25
     */


    /**
     * 이메일 중복확인 API
     *
     * @return
     * @author 이상민
     * @since 2024.01.25
     */
//    fun duplicationEmail(
//        email: String,
//        context: Activity,
//        onResponse: (ApplicationResponse<String>) -> Unit,
//        onFailure: (Throwable) -> Unit,
//    ){
//        RetrofitService.connect(
//            authService.duplicationEmail(DuplicatedEmailRequest(email)),
//            context,
//            onResponse,
//            onFailure
//        )
//    }

}
