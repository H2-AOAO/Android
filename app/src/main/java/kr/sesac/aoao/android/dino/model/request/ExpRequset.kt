package kr.sesac.aoao.android.dino.model.request

import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

data class ExpRequset(
    @SerializedName("currLv") val currLv: Int,
    @SerializedName("currExp") val currExp: Int
)
