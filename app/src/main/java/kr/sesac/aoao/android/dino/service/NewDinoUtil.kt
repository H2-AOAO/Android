package kr.sesac.aoao.android.dino.service

import android.app.Activity
import kr.sesac.aoao.android.common.RetrofitService
import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.common.RetrofitConnection
import kr.sesac.aoao.android.dino.model.request.NewDinoRequest
import retrofit2.create

object NewDinoUtil {
    private val dinoInfoService = RetrofitConnection.getInstance().create(DinoInfoService::class.java);

    /**
     * 새로운 다이노 추가
     * @since 2024.01.26
     * @return ApplicationResponse<Boolean>
     * @author 김은서
     */
    fun newDino(
        accessToken:String,
        newDinoRequest : NewDinoRequest,
        context :Activity,
        onResponse:(ApplicationResponse<Boolean>) -> Unit,
        onFailure: (Throwable) -> Unit
    ){
        RetrofitService.connect(
            dinoInfoService.newDino(accessToken,newDinoRequest),
            context,onResponse,onFailure
        )
    }
}