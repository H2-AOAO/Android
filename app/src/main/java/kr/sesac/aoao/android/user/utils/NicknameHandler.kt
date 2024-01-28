package kr.sesac.aoao.android.user.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.user.model.request.DuplicatedNicknameRequest
import kr.sesac.aoao.android.user.service.AuthRepository

/**
 * 닉네임 검증 Object
 *
 * @since 2024.01.28
 * @author 이상민
 */
@SuppressLint("StaticFieldLeak")
object NicknameHandler {

    private lateinit var context: Context
    private val authRepository = AuthRepository

    fun initializeContext(ctx: Context) {
        context = ctx
    }

    /**
     * 닉네임 검증
     *
     * @since 2024.01.26
     * @author 이상민
     */
    fun handleNicknameValidation(
        nicknameEditText: EditText, // editText 에 적힌 닉네임
        nicknameForDuplicationCheck: String, // 중복 확인 버튼을 눌렀을 때의 닉네임
        nicknameCheckButton: Button,  // 닉네임 중복 확인 버튼
        errorMessageTextView: TextView, // 닉네임 에러/성공 메세지를 보여주는
        isValidNickName : Boolean // 닉네임 검증 값
    ): Triple<String, TextView, Int>  {

        val nickname = nicknameEditText.text.toString()
        if (nicknameEditText.text.toString() != nicknameForDuplicationCheck) {
            nicknameCheckButton.isEnabled = true
        }

        nicknameButtonState(nicknameCheckButton)

        val trimmedNickname = nickname.trim()
        return when {
            nickname.isEmpty() -> { Triple("2자 이상 10자 이내의 한글, 영문, 숫자만 입력 가능합니다.", errorMessageTextView, R.color.hint) }
            trimmedNickname != nickname -> { Triple("닉네임에 공백이 포함되었습니다.", errorMessageTextView, R.color.error) }
            nickname.length == 1 -> { Triple("닉네임이 너무 짧습니다. 2자 이상 입력해 주세요.", errorMessageTextView, R.color.error) }
            !isNicknameValid(nickname) -> { Triple("2자 이상 10자 이내의 한글, 영문, 숫자만 입력 가능합니다.", errorMessageTextView, R.color.error) }
            !isValidNickName -> { Triple("닉네임이 중복되었는지 확인해주세요.", errorMessageTextView, R.color.error) }
            else -> { Triple("", errorMessageTextView, R.color.success) // Success case
            }
        }
    }

    /**
     * 닉네임 버튼 상태 변경
     *
     * @since 2024.01.26
     * @author 이상민
     */
    fun nicknameButtonState(nicknameCheckButton: Button) {
        if(!nicknameCheckButton.isEnabled){
            nicknameCheckButton.setBackgroundColor(ContextCompat.getColor(context,R.color.user_button_success))
        }else{
            nicknameCheckButton.setBackgroundColor(ContextCompat.getColor(context,R.color.user_button))
        }
    }

    /**
     * 닉네임 검증 함수
     *
     * @since 2024.01.25
     * @return boolean
     * @author 이상민
     */
    private fun isNicknameValid(nickname: String): Boolean {
        val regex = Regex("^[a-zA-Z0-9가-힣]{2,10}$")
        return regex.matches(nickname)
    }
}
