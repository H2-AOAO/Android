package kr.sesac.aoao.android.user.service

import android.app.Activity
import kr.sesac.aoao.android.common.RetrofitConnection
import kr.sesac.aoao.android.common.RetrofitService
import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.common.model.ErrorResponse
import kr.sesac.aoao.android.todofolder.model.response.TodoQueryDetailResponse
import kr.sesac.aoao.android.todofolder.service.TodoRepository
import kr.sesac.aoao.android.todofolder.service.TodoService
import kr.sesac.aoao.android.user.model.response.MypageResponse

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


}