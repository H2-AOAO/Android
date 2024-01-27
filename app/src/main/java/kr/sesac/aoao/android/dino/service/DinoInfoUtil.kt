package kr.sesac.aoao.android.dino.service

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.widget.Button
import androidx.core.content.ContextCompat
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.common.RetrofitConnection
import kr.sesac.aoao.android.common.RetrofitService
import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.common.model.ErrorResponse
import kr.sesac.aoao.android.common.model.ItemResponse
import kr.sesac.aoao.android.dino.model.request.ExpRequset
import kr.sesac.aoao.android.dino.model.request.ItemNumRequset
import kr.sesac.aoao.android.dino.model.request.NewDinoRequest
import kr.sesac.aoao.android.dino.model.response.DinoResponse
import kr.sesac.aoao.android.dino.model.response.PastDinoResponse
import kr.sesac.aoao.android.dino.model.response.UserItemResponse

object DinoInfoUtil {
    private val dinoInfoService = RetrofitConnection.getInstance().create(DinoInfoService::class.java);

    /**
     * 다이노 정보 조회
     * @since 2024.01.23
     * @return ApplicationResponse<DinoResponse>
     * @author 김은서
     */
    fun getDinoInfo(
        accessToken:String,
        context : Activity,
        onResponse:(ApplicationResponse<DinoResponse>) -> Unit,
        onFailure: (ErrorResponse) -> Unit
    ){
        RetrofitService.connect(
            dinoInfoService.getDinoInfo(accessToken),
            context,onResponse,onFailure
        )
    }
    /**
     * 아이템 개수 조회
     * @since 2024.01.22
     * @return ApplicationResponse<UserItemResponse>
     * @author 김은서
     */
    fun saveUserItem(
        accessToken:String,
        itemNumRequset: ItemNumRequset,
        context : Activity,
        onResponse:(ApplicationResponse<UserItemResponse>) -> Unit,
        onFailure: (ErrorResponse) -> Unit
    ){
        RetrofitService.connect(
            dinoInfoService.getItemInfo(accessToken, itemNumRequset),
            context,onResponse,onFailure
        )
    }

    /**
     * 경험치 조절
     * @since 2024.01.23
     * @return ApplicationResponse<DinoResponse>
     * @author 김은서
     */
    fun UpExp(
        accessToken:String,
        expRequset: ExpRequset,
        context : Activity,
        onResponse:(ApplicationResponse<DinoResponse>) -> Unit,
        onFailure: (ErrorResponse) -> Unit
    ){
        RetrofitService.connect(
            dinoInfoService.UpEXP(accessToken, expRequset),
            context,onResponse,onFailure
        )
    }

    /**
     * 과거 다이노 조회
     * @since 2024.01.28
     * @return ApplicationResponse<List<PastDino>>
     * @author 김은서
     */
    fun pastDino(
        accessToken: String,
        context: Activity,
        onResponse:(ApplicationResponse<List<PastDinoResponse>>) -> Unit,
        onFailure: (ErrorResponse) -> Unit
    ){
        RetrofitService.connect(
            dinoInfoService.pastDino(accessToken),
            context,onResponse,onFailure
        )
    }


    /**
     * 버튼 비활성화
     * @since 2024.01.23
     * @author 김은서
     */
    fun btDisable(btnItem: Button, context: Context){
        var colorResId = R.color.light_grey //회색으로 색을 변경하여 사용자에게 가시적으로 사용이 불가함을 알림
        val color = ContextCompat.getColor(context, colorResId)
        btnItem.backgroundTintList = ColorStateList.valueOf(color)
        btnItem.isEnabled = false //버튼 비활성화
    }
}
