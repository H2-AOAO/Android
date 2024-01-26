package kr.sesac.aoao.android.user.model.request

import com.google.gson.annotations.SerializedName

data class DuplicatedEmailRequest(
    @SerializedName("email")var email: String? = null
)
