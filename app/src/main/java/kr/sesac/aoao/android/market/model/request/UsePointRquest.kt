package kr.sesac.aoao.android.market.model.request

import com.google.gson.annotations.SerializedName

data class UsePointRquest(
    @SerializedName("itemId") val itemId : Int
)
