package kr.sesac.aoao.android.common.model

import com.google.gson.annotations.SerializedName

data class ApplicationResponse<T>(
    var boolean: Boolean,
    @SerializedName("message") var message : String?,
    var date : T?
)
