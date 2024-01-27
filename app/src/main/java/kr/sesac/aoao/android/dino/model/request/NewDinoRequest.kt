package kr.sesac.aoao.android.dino.model.request

import com.google.gson.annotations.SerializedName

data class NewDinoRequest(
    @SerializedName("dinoColor") val dinoColor: String,
    @SerializedName("dinoName") val dinoName: String
)
