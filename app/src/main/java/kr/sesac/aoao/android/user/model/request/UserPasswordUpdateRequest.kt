package kr.sesac.aoao.android.user.model.request

data class UserPasswordUpdateRequest(
    var password: String? = null,
    var newPassword: String? = null,
    var checkedPassword: String? = null
)
