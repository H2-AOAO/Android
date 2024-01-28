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
import androidx.core.content.ContextCompat
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.databinding.ActivityProfileEditBinding
import kr.sesac.aoao.android.user.service.UserRepository
import kr.sesac.aoao.android.user.utils.NicknameHandler

/**
 * @since 2024.01.28
 * @author 이상민
 */
class ProfileEditActivity : AppCompatActivity(), View.OnClickListener{

    private val userRepository = UserRepository
    private lateinit var binding: ActivityProfileEditBinding

    private lateinit var accessToken: String


    private lateinit var backButton: ImageView
    private lateinit var imgUser : ImageView

    // 닉네임
    private lateinit var changeNickname: EditText
    private lateinit var nicknameErrorMessageTextview: TextView
    private lateinit var changeNicknameCheckButton: Button

    // 비밀번호
    private lateinit var changePw: EditText
    private lateinit var pwCheckErrorMessageTextview: TextView
    private lateinit var changePwCheckButton: Button

    private lateinit var pwCheckButton: Button

    private lateinit var changeButton: Button

    // 상수
    private var nicknameForDuplicationCheck: String = ""
    private var isValidNickName : Boolean = false

    private fun initializeViews() {
        // 뷰 초기화
        backButton = binding.backButton
        imgUser = binding.imgUser

        changeNickname = binding.changeNickname
        nicknameErrorMessageTextview = binding.nicknameErrorMessageTextview
        changeNicknameCheckButton = binding.changeNicknameCheckButton

        changePw = binding.changePw
        changePwCheckButton = binding.changePwCheckButton
        pwCheckErrorMessageTextview = binding.pwCheckErrorMessageTextview

        pwCheckButton = binding.pwCheckButton
        changeButton = binding.changeProfileButton

        // Button
        changeNicknameCheckButton.setOnClickListener(this)
        changePwCheckButton.setOnClickListener(this)
        pwCheckButton.setOnClickListener(this)
        changeButton.setOnClickListener(this)

        // EditText
        changeNickname.addTextChangedListener(textWatcher)
        changePw.addTextChangedListener(textWatcher)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeViews()
    }

    /**
     * EditText 값이 변경될 때 호출
     *
     * @since 2024.01.26
     * @author 이상민
     */
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(editable: Editable?) {

            val (message, textView, color) = NicknameHandler.handleNicknameValidation(
                changeNickname,
                nicknameForDuplicationCheck,
                changeNicknameCheckButton,
                nicknameErrorMessageTextview,
                isValidNickName
            )
            updateUIOnUiThread(message, textView, color)
        }
    }


    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.back_button -> {
                onBackPressed()
            }
            R.id.changeNicknameCheckButton -> {

            }
            R.id.changePwCheckButton -> {

            }
            R.id.pwCheckButton -> {
                val intent = Intent(this@ProfileEditActivity, PasswordEditActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.changeProfileButton -> {

            }
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
            textView.setTextColor(ContextCompat.getColor(this@ProfileEditActivity, color))
        }
    }
}