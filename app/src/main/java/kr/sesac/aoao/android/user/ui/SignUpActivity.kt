package kr.sesac.aoao.android.user.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.common.RetrofitConnection
import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.databinding.ActivitySignUpBinding
import kr.sesac.aoao.android.user.model.request.DuplicatedEmailRequest
import kr.sesac.aoao.android.user.model.request.DuplicatedNicknameRequest
import kr.sesac.aoao.android.user.model.request.SignupRequest
import kr.sesac.aoao.android.user.model.request.UserPasswordUpdateRequest
import kr.sesac.aoao.android.user.model.response.SignupResponse
import kr.sesac.aoao.android.user.service.AuthRepository
import kr.sesac.aoao.android.user.service.AuthService
import kr.sesac.aoao.android.user.utils.NicknameHandler
import kr.sesac.aoao.android.user.utils.NicknameHandler.nicknameButtonState
import kr.sesac.aoao.android.user.utils.PasswordValidator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @since 2024.01.23
 * @author 이상민
 */
class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    private val authRepository = AuthRepository
    private lateinit var binding: ActivitySignUpBinding

    private lateinit var backButton: ImageView

    private lateinit var signEmail: EditText
    private lateinit var emailErrorMessageTextview: TextView
    private lateinit var emailCheckButton: Button

    private lateinit var signNickname: EditText
    private lateinit var signupNicknameErrorMessageTextview: TextView
    private lateinit var nicknameCheckButton: Button

    private lateinit var signPw: EditText
    private lateinit var pwErrorMessageTextview: TextView

    private lateinit var signPw2: EditText
    private lateinit var pw2ErrorMessageTextview: TextView

    private lateinit var signupButton: Button

    private var emailForDuplicationCheck: String = "" // 중복 확인 버튼이 눌린 후의 이메일 값을 저장하는 변수
    private var nicknameForDuplicationCheck: String = ""

    private var isValidEmail : Boolean = false // 중복 버튼 확인
    private var isValidNickName : Boolean = false

    /**
     * 뷰 초기화
     *
     * @since 2024.01.26
     * @author 이상민
     */
    private fun initializeViews() {
        backButton = binding.backButton
        signupButton = binding.signupButton
        // 이메일
        signEmail = binding.signEmail
        emailErrorMessageTextview = binding.signEmailErrorMessageTextview
        emailCheckButton = binding.emailCheckButton
        // 닉네임
        signNickname = binding.signNickname
        signupNicknameErrorMessageTextview = binding.signupNicknameErrorMessageTextview
        nicknameCheckButton = binding.signupNicknameCheckButton
        // 비밀번호
        signPw = binding.signPw
        pwErrorMessageTextview = binding.signupPwErrorMessageTextview
        // 체크 비밀번호
        signPw2 = binding.singPw2
        pw2ErrorMessageTextview = binding.signupPw2ErrorMessageTextview
    }

    /**
     * 리스너 추가
     *
     * @since 2024.01.26
     * @author 이상민
     */
    private fun setListeners() {
        // Button
        backButton.setOnClickListener(this)
        emailCheckButton.setOnClickListener(this)
        nicknameCheckButton.setOnClickListener(this)
        signupButton.setOnClickListener(this)

        // EditText
        signEmail.addTextChangedListener(textWatcher)
        signNickname.addTextChangedListener(textWatcher)
        signPw.addTextChangedListener(textWatcher)
        signPw2.addTextChangedListener(textWatcher)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeViews()
        setListeners()

        window.statusBarColor = ContextCompat.getColor(this, R.color.white) //상태바 색깔 하얀색
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR //상태바 글자색 검은색

        NicknameHandler.initializeContext(this)
        PasswordValidator.initializeContext(this)
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
            validatedAll()
        }
    }

    private fun validatedAll(){
        if(signEmail.text.toString() != emailForDuplicationCheck) {
            emailCheckButton.isEnabled = true
        }
        emailButtonState()

        // 닉네임 검증
        val (nicknameMessage, nicknameTextView, nicknameColor) = NicknameHandler.handleNicknameValidation(
            signNickname,
            nicknameForDuplicationCheck,
            nicknameCheckButton,
            signupNicknameErrorMessageTextview,
            isValidNickName
        )
        updateUIOnUiThread(nicknameMessage, nicknameTextView, nicknameColor)

        // 비밀번호1 검증
        val (passwordMessage, passwordTextView, passwordColor) = PasswordValidator.validatePassword1(
            signPw,
            pwErrorMessageTextview
        )
        updateUIOnUiThread(passwordMessage, passwordTextView, passwordColor)

        // 비밀번호2 검증
        val (password2Message, password2TextView, password2Color) = PasswordValidator.validatePassword2(
            signPw2,
            signPw,
            pw2ErrorMessageTextview
        )
        updateUIOnUiThread(password2Message, password2TextView, password2Color)
    }

    /**
     * 버튼 이벤트
     *
     * @since 2024.01.24
     * @author 이상민
     */
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.back_button -> {
                onBackPressed()
            }

            R.id.emailCheckButton -> {
                if(signEmail.text?.trim() == ""){
                    updateUIOnUiThread("이메일을 입력해주세요.", emailErrorMessageTextview, R.color.error)
                }else{
                    duplicationEmail(DuplicatedEmailRequest(signEmail.text?.toString()))
                }
            }

            R.id.signupNicknameCheckButton -> {
                val (message, textView, color) = NicknameHandler.handleNicknameValidation(
                    signNickname,
                    nicknameForDuplicationCheck,
                    nicknameCheckButton,
                    signupNicknameErrorMessageTextview,
                    isValidNickName
                )
                updateUIOnUiThread(message, textView, color)

                if(signNickname.text.trim() == ""){
                    updateUIOnUiThread("닉네임을 입력해주세요.", signupNicknameErrorMessageTextview, R.color.error)
                }else{
                    duplicationNickname(DuplicatedNicknameRequest(signNickname.text.toString()))
                }
            }

            R.id.signupButton -> {
                if(signPw.text?.trim() == ""){
                    updateUIOnUiThread("이메일을 입력해주세요.", emailErrorMessageTextview, R.color.error)
                }
                if(signNickname.text?.trim() == ""){
                    updateUIOnUiThread("닉네임을 입력해주세요.", signupNicknameErrorMessageTextview, R.color.error)
                }
                if(signPw.text?.trim() == ""){
                    updateUIOnUiThread("비밀번호를 입력해주세요.", pwErrorMessageTextview, R.color.error)
                }
                validatedAll()

                if(isValidEmail
                    && isValidNickName
                    && (signPw.text.toString() == signPw2.text.toString())){
                    val signupRequest = SignupRequest(signEmail.text.toString()
                        ,signNickname.text.toString()
                        ,signPw.text.toString()
                        ,signPw2.text.toString())
                    signup(signupRequest)
                }
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
            textView.setTextColor(ContextCompat.getColor(this, color))
        }
    }

    /**
     * 이메일 중복 API 호출
     *
     * @since 2024.01.25
     * @author 이상민
     */
    private fun duplicationEmail(request: DuplicatedEmailRequest) {
        authRepository.duplicationEmail(request,
            this,
            onResponse = { response ->
                if (response.success && response.date != null) {

                    updateUIOnUiThread(response.date!!, emailErrorMessageTextview, R.color.success)
                    isValidEmail = true
                    emailForDuplicationCheck = signEmail.text.toString()
                    emailCheckButton.isEnabled = false  // 버튼 비활성화
                    emailButtonState()
                }
            },
            onFailure = { response ->
                updateUIOnUiThread(response.message.toString(), emailErrorMessageTextview, R.color.error)
                isValidEmail = false

                emailForDuplicationCheck = ""
                emailCheckButton.isEnabled = true  // 버튼 활성화

                emailButtonState()
            })
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
                    updateUIOnUiThread(response.date!!, signupNicknameErrorMessageTextview, R.color.success)
                    isValidNickName = true
                    nicknameForDuplicationCheck = signNickname.text.toString()
                    nicknameCheckButton.isEnabled = false  // 버튼 비활성화
                    nicknameButtonState(nicknameCheckButton)
                }
            },
            onFailure = { response ->
                if (response.date != null) {
                    response.message?.let { updateUIOnUiThread(it, signupNicknameErrorMessageTextview, R.color.error) }
                    isValidNickName = false
                    nicknameCheckButton.isEnabled = true  // 버튼 활성화
                    nicknameButtonState(nicknameCheckButton)
                }
            })
    }

    /**
     * 회원가입 API 호출
     *
     * @since 2024.01.25
     * @author 이상민
     */
    fun signup(request: SignupRequest) {
        authRepository.signup(request,
            this,
            onResponse = { response ->
                if(response.date != null){
                    successSignup()
                }

            },
            onFailure = { response ->
            })
    }

    private fun successSignup() {
        Toast.makeText(this, "회원 가입 되었습니다.", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // 현재 회원 가입 화면 종료
    }

    /**
     * 이메일 버튼 상태 변경
     *
     * @since 2024.01.26
     * @author 이상민
     */
    private fun emailButtonState(){
        if(!emailCheckButton.isEnabled){
            emailCheckButton.setBackgroundColor(ContextCompat.getColor(this,R.color.user_button_success))
        }else{
            emailCheckButton.setBackgroundColor(ContextCompat.getColor(this,R.color.user_button))
        }
    }

}
