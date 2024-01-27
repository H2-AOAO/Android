package kr.sesac.aoao.android.user.model.response

data class TokenResponse(
    val userId: Long,
    val accessToken: String,
    val refreshToken: String
)