package kr.sesac.aoao.android.common.model

import com.google.gson.annotations.SerializedName

data class ApplicationResponse<T>(
    var success : Boolean,
    @SerializedName("message") var message : String?,
    @SerializedName("date") var date : T?
)
