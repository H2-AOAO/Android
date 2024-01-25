package kr.sesac.aoao.android.user.model.response

data class SignupResponse(
    var userId: Int? = null,
    var email: String? = null,
    var nickname: String? = null,
    var password: String? = null
)
