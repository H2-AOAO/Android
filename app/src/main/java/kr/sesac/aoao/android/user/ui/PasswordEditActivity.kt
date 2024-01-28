package kr.sesac.aoao.android.user.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.common.TokenManager
import kr.sesac.aoao.android.databinding.ActivityMyPageBinding
import kr.sesac.aoao.android.databinding.ActivityPasswordEditBinding
import kr.sesac.aoao.android.user.model.request.DuplicatedEmailRequest
import kr.sesac.aoao.android.user.model.request.UserPasswordUpdateRequest
import kr.sesac.aoao.android.user.service.UserRepository
import kr.sesac.aoao.android.user.utils.PasswordValidator

/**
 * @since 2024.01.28
 * @author 이상민
 */
class PasswordEditActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityPasswordEditBinding
    private val userRepository = UserRepository
    private lateinit var accessToken: String

    private lateinit var backButton: ImageView

    // 현재 비밀번호
    private lateinit var currentPw: EditText
    private lateinit var currentPwErrorMessageTextview: TextView

    // 새 비밀번호
    private lateinit var newPw: EditText
    private lateinit var pwErrorMessageTextview: TextView

    // 확인 비밀번호
    private lateinit var newPw2: EditText
    private lateinit var pw2ErrorMessageTextview: TextView

    private lateinit var changeButton: Button

    private var newPwContent = ""
    private var newPwCheckContent = ""

    private fun initializeViews() {
        backButton = binding.passwordBackButton
        changeButton = binding.changePasswordButton

        currentPw = binding.currentPw
        currentPwErrorMessageTextview = binding.currentPwErrorMessageTextview

        newPw = binding.newPw
        pwErrorMessageTextview = binding.pwErrorMessageTextview

        newPw2 = binding.newPw2
        pw2ErrorMessageTextview = binding.pw2ErrorMessageTextview

        // 클릭 이벤트
        changeButton.setOnClickListener(this)

        // EditText
        currentPw.addTextChangedListener(textWatcher)
        newPw.addTextChangedListener(textWatcher)
        newPw2.addTextChangedListener(textWatcher)
    }

    /**
     * EditText 값이 변경될 때 호출
     *
     * @since 2024.01.26
     * @author 이상민
     */
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(
            charSequence: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(
            charSequence: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) {
        }

        override fun afterTextChanged(editable: Editable?) {

            changeButton.isEnabled = newPw.text.toString() == newPw2.text.toString()

            // 현재 비밀번호
            val (currentPasswordMessage, currentPasswordTextView, currentPasswordColor) = PasswordValidator.validatePassword1(
                currentPw,
                pwErrorMessageTextview
            )
            updateUIOnUiThread(
                currentPasswordMessage,
                currentPasswordTextView,
                currentPasswordColor
            )

            // 비밀번호1 검증
            val (passwordMessage, passwordTextView, passwordColor) = PasswordValidator.validatePassword1(
                newPw,
                pwErrorMessageTextview
            )
            updateUIOnUiThread(passwordMessage, passwordTextView, passwordColor)

            // 비밀번호2 검증
            val (password2Message, password2TextView, password2Color) = PasswordValidator.validatePassword2(
                newPw2,
                newPw,
                pw2ErrorMessageTextview
            )
            updateUIOnUiThread(password2Message, password2TextView, password2Color)
        }
    }

    /**
     * UI 업데이트를 위한 runOnUiThread 사용
     *
     * @since 2024.01.25
     * @author 이상민
     */
    private fun updateUIOnUiThread(message: String, textView: TextView, color: Int) {
        runOnUiThread {
            textView.text = message
            textView.setTextColor(ContextCompat.getColor(this@PasswordEditActivity, color))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeViews()
        accessToken = TokenManager.getAccessTokenWithTokenType(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.passwordBackButton -> {

                val intent = Intent(this, ProfileEditActivity::class.java)
                startActivity(intent)
                finish()
            }

            R.id.changePasswordButton -> {
                newPwContent = newPw.text.toString()
                newPwCheckContent = newPw2.text.toString()

                if (currentPw.text.toString() == newPw2.text.toString()) {
                    updateUIOnUiThread(
                        "변경 사항이 존재하지 않습니다.",
                        currentPwErrorMessageTextview,
                        R.color.error
                    )
                } else {
                    if (newPw.text.toString() == newPw2.text.toString()) {
                        updatePassword(
                            UserPasswordUpdateRequest(currentPw.text.toString(), newPw.text.toString(), newPw2.text.toString())
                        )
                    }
                }

            }
        }
    }

    /**
     * 비밀번호 변경 API 호출
     *
     * @since 2024.01.28
     * @author 이상민
     */
    private fun updatePassword(request: UserPasswordUpdateRequest) {
        userRepository.updatePassword(accessToken, request,
            this,
            onResponse = { response ->
                if (response.success == true) {
                    Toast.makeText(this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ProfileEditActivity::class.java)
                    startActivity(intent)
                }
            },
            onFailure = { response ->
                // 현재 비밀번호가 다를 때 - 잘못된 비밀번호
                updateUIOnUiThread("잘못된 비밀번호입니다.", currentPwErrorMessageTextview, R.color.error)
            })
    }
}
