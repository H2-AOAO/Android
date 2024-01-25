package kr.sesac.aoao.android.user.model.request

data class SignupRequest (
    var email: String? = null,
    var nickname: String? = null,
    var password: String? = null,
    var checkedPassword: String? = null
)