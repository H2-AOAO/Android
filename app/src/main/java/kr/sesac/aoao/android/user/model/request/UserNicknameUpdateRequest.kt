package kr.sesac.aoao.android.user.model.request

data class UserNicknameUpdateRequest (
    var nickname: String? = null,
    var password: String? = null
)