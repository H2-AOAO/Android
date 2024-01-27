package kr.sesac.aoao.android.common

import android.app.Activity
import android.util.Log
import com.google.gson.Gson
import kr.sesac.aoao.android.common.model.ErrorResponse

object ErrorMessageGenerator {
    fun returnError(errorCode : String?, context: Activity){ //에러코드 알려주기
        try {
            val errorMsg = Gson().fromJson(errorCode, ErrorResponse::class.java).message //에러 바디에서 에러 메시지만 뽑아내기
            ToastGenerator.showShortToast(errorMsg, context)
            Log.e("응답", "Error Message: $errorMsg")
        } catch (e: Exception) {
            Log.e("응답", "Error parsing error body: ${e.message}")
        }
    }
}