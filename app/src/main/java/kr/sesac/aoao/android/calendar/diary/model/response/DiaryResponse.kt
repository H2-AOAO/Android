package kr.sesac.aoao.android.calendar.diary.model.response

/**
 * @since 2024.01.28
 * @author 김유빈
 */
data class DiaryResponse(
    val diaryId: Long,
    val content: String,
    val date: String
)
