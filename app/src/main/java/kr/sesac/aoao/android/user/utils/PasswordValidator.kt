package kr.sesac.aoao.android.user.utils

import android.annotation.SuppressLint
import android.content.Context
import android.widget.EditText
import android.widget.TextView
import kr.sesac.aoao.android.R

/**
 * 비밀번호 검증 Object
 *
 * @since 2024.01.28
 * @author 이상민
 */
@SuppressLint("StaticFieldLeak")
object PasswordValidator {

    private lateinit var context: Context

    fun initializeContext(ctx: Context) {
        context = ctx
    }

    /**
     * 비밀번호1 검증
     *
     * @since 2024.01.26
     * @param passwordEdit 입력받은 비밀번호
     * @param errorTextView 오류 메시지를 표시
     * @param isValidEmail
     * @return Triple 객체 (message: String, textView: TextView, color: Int)
     */
    fun validatePassword1(
        passwordEdit: EditText,
        errorTextView: TextView
    ): Triple<String, TextView, Int> {
        val password = passwordEdit.text.toString()
        val trimmedPassword = password.trim()
        return when {
            password.isEmpty() -> Triple("영문, 숫자, 특수문자를 포함한 8~20자여야 합니다.", errorTextView, R.color.hint)
            trimmedPassword != password -> Triple("비밀번호에 공백이 포함되었습니다.", errorTextView, R.color.error)
            !isPasswordValid(password) || password.trim().length > 20 -> Triple("영문, 숫자, 특수문자를 포함한 8~20자여야 합니다.", errorTextView, R.color.error)
            else -> Triple("사용 가능한 비밀번호입니다.", errorTextView, R.color.success)
        }
    }

    /**
     * 비밀번호2 검증
     *
     * @since 2024.01.26
     * @author 이상민
     */
    fun validatePassword2(
        password2EditText: EditText,
        password1EditText: EditText,
        errorTextView: TextView
    ): Triple<String, TextView, Int> {
        val password2 = password2EditText.text.toString()

        if (password2.isEmpty()) {
            return Triple("", errorTextView, R.color.success)
        } else if (password1EditText.text.toString() != password2) {
            return Triple("비밀번호가 일치하지 않습니다.", errorTextView, R.color.error)
        } else {
            return Triple("비밀번호가 일치합니다.", errorTextView, R.color.success)
        }
    }

    /**
     * 비밀번호 검증 함수
     * @since 2024.01.25
     * @return boolean
     * @author 이상민
     */
    private fun isPasswordValid(password: String): Boolean {
        // 원하는 조건에 따라 검사
        val regex = """^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,20}$""".toRegex()
        return regex.matches(password)
    }


}