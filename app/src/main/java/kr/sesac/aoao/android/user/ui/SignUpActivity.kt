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
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.common.RetrofitConnection
import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.common.model.ErrorResponse
import kr.sesac.aoao.android.databinding.ActivitySignUpBinding
import kr.sesac.aoao.android.user.model.request.DuplicatedEmailRequest
import kr.sesac.aoao.android.user.model.request.DuplicatedNicknameRequest
import kr.sesac.aoao.android.user.model.request.SignupRequest
import kr.sesac.aoao.android.user.model.response.SignupResponse
import kr.sesac.aoao.android.user.service.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @since 2024.01.23
 * @author 이상민
 */
class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySignUpBinding

    private lateinit var backButton: ImageView

    private lateinit var sign_email: EditText
    private lateinit var email_error_message_textview: TextView
    private lateinit var email_check_button: Button

    private lateinit var sign_nickname: EditText
    private lateinit var nickname_error_message_textview: TextView
    private lateinit var nickname_check_button: Button

    private lateinit var sign_pw: EditText
    private lateinit var pw_error_message_textview: TextView

    private lateinit var sing_pw2: EditText
    private lateinit var pw2_error_message_textview: TextView

    private lateinit var signup_button: Button

    private var input_email: String = ""
    private var input_nickname: String = ""
    private var input_pw: String = ""
    private var input_pw_check: String = ""

    private var isValidEmail : Boolean = false
    private var isValidNickName : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        backButton = binding.backButton
        backButton.setOnClickListener(this)

        // 이메일
        sign_email = binding.signEmail
        email_error_message_textview = binding.emailErrorMessageTextview
        email_check_button = binding.emailCheckButton
        email_check_button.setOnClickListener(this)

        // 닉네임
        sign_nickname = binding.signNickname
        nickname_error_message_textview = binding.nicknameErrorMessageTextview
        nickname_check_button = binding.nicknameCheckButton
        nickname_check_button.setOnClickListener(this)

        // 비밀번호
        sign_pw = binding.signPw
        pw_error_message_textview = binding.pwErrorMessageTextview

        // 비밀번호 확인
        sing_pw2 = binding.singPw2
        pw2_error_message_textview = binding.pw2ErrorMessageTextview

        // 회원가입 버튼
        signup_button = binding.signupButton
        signup_button.setOnClickListener(this)

        // 값 변경 이벤트 추가
        sign_email.addTextChangedListener(textWatcher)
        sign_pw.addTextChangedListener(textWatcher)
        sing_pw2.addTextChangedListener(textWatcher)
    }

    /**
     * EditText 값이 변경될 때 호출
     * @since 2024.01.26
     * @author 이상민
     */
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(editable: Editable?) {

            val nickname = sign_nickname.text.toString()
            val password = sign_pw.text.toString()
            val password2 = sing_pw2.text.toString()

            // 닉네임 검증
            val trimmedNickname = nickname.trim()
            if (nickname.isEmpty()) {
                updateUIOnUiThread("2자 이상 10자 이내의 한글, 영문, 숫자만 입력 가능합니다.", nickname_error_message_textview, R.color.hint)
            } else if (trimmedNickname != nickname) {
                updateUIOnUiThread("닉네임에 공백이 포함되었습니다.", nickname_error_message_textview, R.color.error)
            } else if (!isNicknameValid(trimmedNickname)) {
                updateUIOnUiThread("2자 이상 10자 이내의 한글, 영문, 숫자만 입력 가능합니다.", nickname_error_message_textview, R.color.error)
            } else if (!isValidNickName) {
                updateUIOnUiThread("닉네임이 중복되었는지 확인해주세요.", nickname_error_message_textview, R.color.success)
            }

            // 비밀번호 검증
            val trimmedPassword = password.trim()
            if (password.isEmpty()) {
                updateUIOnUiThread("영문, 숫자, 특수문자를 포함한 8~20자여야 합니다.", pw_error_message_textview, R.color.hint)
            }else if(trimmedPassword != password) {
                updateUIOnUiThread("비밀번호에 공백이 포함되었습니다.", pw_error_message_textview, R.color.error)
            } else if (!isPasswordValid(password) || password.trim().length > 20 ) {
                updateUIOnUiThread("영문, 숫자, 특수문자를 포함한 8~20자여야 합니다.", pw_error_message_textview, R.color.error)
            } else if (!isValidEmail) {
                updateUIOnUiThread("사용 가능한 비밀번호입니다.", pw_error_message_textview, R.color.success)
            }

            // 비밀번호 확인 검증
            if (password2.isEmpty()) {
                pw2_error_message_textview.text = ""
            } else if (password != password2) {
                updateUIOnUiThread("비밀번호가 일치하지 않습니다.", pw2_error_message_textview, R.color.error)
            } else {
                updateUIOnUiThread("비밀번호가 일치합니다.", pw2_error_message_textview, R.color.success)
            }

        }
    }

    /**
     * 버튼 이벤트 
     * @since 2024.01.24
     * @author 이상민
     */
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.back_button -> {back()}
            R.id.email_check_button -> {
                input_email = sign_email.text?.toString() ?: ""
                if(input_email.trim() == ""){
                    updateUIOnUiThread("이메일을 입력해주세요.", email_error_message_textview, R.color.error)
                }else{
                    checkDuplicatedEmail(DuplicatedEmailRequest(input_email))
                }
            }
            R.id.nickname_check_button -> {
                input_nickname = sign_nickname.text?.toString() ?: ""
                if(input_nickname.trim() == ""){
                    updateUIOnUiThread("닉네임을 입력해주세요.", nickname_error_message_textview, R.color.error)
                }else{
                    checkDuplicatedNickname(DuplicatedNicknameRequest(input_nickname))
                }
            }
            R.id.signup_button -> {
                input_pw = sign_pw.text?.toString() ?: ""  // 첫 번째 비밀번호 입력
                input_pw_check = sing_pw2.text?.toString() ?: ""  // 두 번째 비밀번호 확인 입력

                if(isValidEmail && isValidNickName && (input_pw != input_pw_check)){
                    val signupRequest = SignupRequest(input_email, input_nickname, input_pw, input_pw_check)
                    signup(signupRequest)
                }
                if(input_pw.trim() == ""){
                    updateUIOnUiThread("비밀번호를 입력해주세요.", pw_error_message_textview, R.color.error)
                }
                if(input_pw_check.trim() == ""){
                    updateUIOnUiThread("비밀번호를 입력해주세요.", pw2_error_message_textview, R.color.error)
                }
            }
        }
    }

    /**
     * 뒤로가기 버튼 이벤트 
     * @since 2024.01.24
     * @author 이상민
     */
    private fun back() {
        onBackPressed()  // 뒤로 가기 버튼 동작 추가
    }

    /**
     * UI 업데이트를 위한 runOnUiThread 사용
     * @since 2024.01.25
     * @author 이상민
     */
    private fun updateUIOnUiThread(message: String, textView: TextView, color: Int) {
        runOnUiThread {
            textView.text = message
            textView.setTextColor(ContextCompat.getColor(this@SignUpActivity, color))
        }
    }

    /**
     * 이메일 중복 확인 버튼 이벤트 
     * @since 2024.01.25
     * @author 이상민
     */
    private fun checkDuplicatedEmail(request: DuplicatedEmailRequest) {
        val service = RetrofitConnection.getInstance().create(UserService::class.java)
        service.duplicationEmail(request).enqueue(object : Callback<ApplicationResponse<String>> {
            override fun onResponse(
                call: Call<ApplicationResponse<String>>,
                response: Response<ApplicationResponse<String>>
            ) {
                val applicationResponse = response.body()
                if(applicationResponse?.success == true){
//                    Log.e("이메일 중복확인 : " , "${applicationResponse}")
                    val successBodyString = applicationResponse.date
                    if (successBodyString != null) {
                        updateUIOnUiThread(successBodyString, email_error_message_textview, R.color.success)
                        isValidEmail = true
                    }
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    val message = Gson().fromJson(errorBodyString, ErrorResponse::class.java).message
                    if (message != null) {
                        updateUIOnUiThread(message, email_error_message_textview, R.color.error)
                        isValidEmail = false
                    }
                }
            }

            override fun onFailure(call: Call<ApplicationResponse<String>>, t: Throwable) {
                Log.e("통신 실패: ", t.localizedMessage)
            }
        })
    }

    /**
     * 닉네임 중복 확인 버튼 이벤트
     * @since 2024.01.25
     * @author 이상민
     */
    private fun checkDuplicatedNickname(request: DuplicatedNicknameRequest) {
        val service = RetrofitConnection.getInstance().create(UserService::class.java)
        service.duplicationNickname(request).enqueue(object : Callback<ApplicationResponse<String>> {
            override fun onResponse(
                call: Call<ApplicationResponse<String>>,
                response: Response<ApplicationResponse<String>>
            ) {
                val applicationResponse = response.body()
                if(applicationResponse?.success == true){
//                    Log.e("이메일 중복확인 : " , "${applicationResponse}")
                    val successBodyString = applicationResponse.date
                    if (successBodyString != null) {
                        updateUIOnUiThread(successBodyString, nickname_error_message_textview, R.color.success)
                        isValidNickName = true
                    }
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    val message = Gson().fromJson(errorBodyString, ErrorResponse::class.java).message
                    if (message != null) {
                        updateUIOnUiThread(message, nickname_error_message_textview, R.color.error)
                        isValidNickName = false
                    }
                }
            }

            override fun onFailure(call: Call<ApplicationResponse<String>>, t: Throwable) {
                Log.e("통신 실패: ", t.localizedMessage)
            }
        })
    }

    /**
     * 닉네임 검증 함수
     * @since 2024.01.25
     * @return boolean
     * @author 이상민
     */
    private fun isNicknameValid(nickname: String): Boolean {
        val regex = Regex("^[a-zA-Z0-9가-힣]{2,10}$")
        return regex.matches(nickname)
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

    /**
     * 회원가입 성공 시
     * @since 2024.01.25
     * @author 이상민
     */
    private fun signup(signupRequest: SignupRequest) {
        val service = RetrofitConnection.getInstance().create(UserService::class.java)
        service.signup(signupRequest).enqueue(object : Callback<ApplicationResponse<SignupResponse>> {
            override fun onResponse(
                call: Call<ApplicationResponse<SignupResponse>>,
                response: Response<ApplicationResponse<SignupResponse>>
            ) {
                val applicationResponse = response.body()
                if (applicationResponse?.success == true) {
                    successSignup()
                }
            }
            override fun onFailure(call: Call<ApplicationResponse<SignupResponse>>, t: Throwable) {
                // 요청이 실패한 경우
                Log.e("통신 실패: ", t.localizedMessage)
            }
        })
    }

    private fun successSignup() {
        val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
        startActivity(intent)
        finish() // 현재 회원 가입 화면 종료
    }

}