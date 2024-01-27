package kr.sesac.aoao.android.dino.model.request

import com.google.gson.annotations.SerializedName

data class ItemNumRequset(
    @SerializedName("itemId") val itemId: Int,
    @SerializedName("status") val status: String
)
