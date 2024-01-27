package kr.sesac.aoao.android.user.model.response

data class MypageResponse(
    val email: String,
    val month: String,
    val monthSumTodo: Int,
    val nickname: String,
    val profile: String,
    val sumTodo: Int,
    val today: String
)
