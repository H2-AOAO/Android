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
import kr.sesac.aoao.android.databinding.ActivityNicknameBinding
import kr.sesac.aoao.android.databinding.ActivityProfileEditBinding
import kr.sesac.aoao.android.user.model.request.DuplicatedNicknameRequest
import kr.sesac.aoao.android.user.service.AuthRepository
import kr.sesac.aoao.android.user.service.AuthRepository.duplicationNickname
import kr.sesac.aoao.android.user.service.UserRepository
import kr.sesac.aoao.android.user.utils.FilePicker
import kr.sesac.aoao.android.user.utils.NicknameHandler
import kr.sesac.aoao.android.user.utils.PasswordValidator

class NicknameActivity : AppCompatActivity(), View.OnClickListener {

    private val userRepository = UserRepository
    private var authRepository = AuthRepository

    private lateinit var binding: ActivityNicknameBinding

    private lateinit var accessToken: String

    private lateinit var backButton: ImageView

    // 닉네임
    private lateinit var changeNickname: EditText
    private lateinit var nicknameErrorMessageTextview: TextView
    private lateinit var editNicknameCheckButton: Button

    // 비밀번호
    private lateinit var checkPw: EditText
    private lateinit var pwCheckErrorMessageTextview: TextView

    private lateinit var changeNicknameButton: Button

    // 상수
    private var nicknameForDuplicationCheck: String = ""
    private var isValidNickName : Boolean = false

    private fun initializeViews() {
        // 뷰 초기화
        backButton = binding.backButton

        changeNickname = binding.changeNickname
        nicknameErrorMessageTextview = binding.nicknameErrorMessageTextview
        editNicknameCheckButton = binding.editNicknameCheckButton

        checkPw = binding.checkPw
        pwCheckErrorMessageTextview = binding.pwCheckErrorMessageTextview

        changeNicknameButton = binding.changeNicknameButton

        // button
        backButton.setOnClickListener(this)
        editNicknameCheckButton.setOnClickListener(this)
        changeNicknameButton.setOnClickListener(this)

        // EditText
        changeNickname.addTextChangedListener(textWatcher)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.backButton ->{
                val intent = Intent(this, ProfileEditActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.editNicknameCheckButton -> {
                // 닉네임 중복 체크
                val (message, textView, color) = NicknameHandler.handleNicknameValidation(
                    changeNickname,
                    nicknameForDuplicationCheck,
                    editNicknameCheckButton,
                    nicknameErrorMessageTextview,
                    isValidNickName
                )
                updateUIOnUiThread(message, textView, color)

                if(changeNickname.text.trim() == ""){
                    updateUIOnUiThread("닉네임을 입력해주세요.", nicknameErrorMessageTextview, R.color.error)
                }else{
                    duplicationNickname(DuplicatedNicknameRequest(changeNickname.text.toString()))
                }

            }
            R.id.changeNicknameButton -> {
                val intent = Intent(this@NicknameActivity, ProfileEditActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNicknameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white) //상태바 색깔 하얀색
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR //상태바 글자색 검은색

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
                editNicknameCheckButton,
                nicknameErrorMessageTextview,
                isValidNickName
            )
            updateUIOnUiThread(message, textView, color)

            // 비밀번호1 검증
            val (passwordMessage, passwordTextView, passwordColor) = PasswordValidator.validatePassword1(
                checkPw,
                pwCheckErrorMessageTextview
            )
            updateUIOnUiThread(passwordMessage, passwordTextView, passwordColor)
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
            textView.setTextColor(ContextCompat.getColor(this@NicknameActivity, color))
        }
    }

    /**
     * 닉네임 중복 API 호출
     *
     * @since 2024.01.25
     * @author 이상민
     */
    fun duplicationNickname(request: DuplicatedNicknameRequest) {
        authRepository.duplicationNickname(request,
            this,
            onResponse = { response ->
                if (response.success && response.date != null) {
                    updateUIOnUiThread(response.date!!, nicknameErrorMessageTextview, R.color.success)
                    isValidNickName = true
                    nicknameForDuplicationCheck = changeNickname.text.toString()
                    editNicknameCheckButton.isEnabled = false  // 버튼 비활성화
                    NicknameHandler.nicknameButtonState(editNicknameCheckButton)
                }
            },
            onFailure = { response ->
                if (response.date != null) {
                    response.message?.let { updateUIOnUiThread(it, nicknameErrorMessageTextview, R.color.error) }
                    isValidNickName = false
                    editNicknameCheckButton.isEnabled = true  // 버튼 활성화
                    NicknameHandler.nicknameButtonState(editNicknameCheckButton)
                }
            })
    }
}