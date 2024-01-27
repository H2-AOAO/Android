package kr.sesac.aoao.android.market.service

import android.app.Activity
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.common.RetrofitConnection
import kr.sesac.aoao.android.common.RetrofitService
import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.common.model.ErrorResponse
import kr.sesac.aoao.android.market.model.request.UsePointRquest
import kr.sesac.aoao.android.market.model.response.MarketResponse

object MarketUtil {
    fun getMappingItemIMG(name:String) : (Int){
        val drawableResId = when (name) { //각 이름에 맞는 이미지 파일로 매핑
            "item_food" -> R.drawable.item_food
            "item_medicine" -> R.drawable.item_medicine
            "item_play" -> R.drawable.item_play
            "item_love" -> R.drawable.item_love
            else -> R.drawable.item_set
        }
        return drawableResId
    }

    fun getMappingItemName(name:String) : (String){
        val item_name = when(name){ //각 이름에 맞는 이름으로 매핑
            "item_food" -> "사료"
            "item_medicine" -> "약품"
            "item_play" -> "놀이"
            "item_love" -> "애정"
            else -> "1 세트"
        }
        return item_name
    }

    val marketService = RetrofitConnection.getInstance().create(MarketService::class.java);

    fun usePoint(
        accessToken : String,
        usePointRquest: UsePointRquest,
        context : Activity,
        onResponse: (ApplicationResponse<MarketResponse>) -> Unit,
        onFailure: (ErrorResponse) -> Unit
    ){
        RetrofitService.connect(
            marketService.usePoint(accessToken,usePointRquest),
            context,onResponse,onFailure
        )
    }
}