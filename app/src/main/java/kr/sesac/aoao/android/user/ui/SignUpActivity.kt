package kr.sesac.aoao.android.user.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import kr.sesac.aoao.android.R

class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var backButton: ImageView

    private lateinit var sign_email: EditText
    private lateinit var email_error_message_textview: TextView
    private lateinit var email_check_button: Button

    private lateinit var nickname_check_button: Button
    private lateinit var nickname_hint: TextView

    private lateinit var pw_hint: TextView
    private lateinit var pw2_hint: TextView
    private lateinit var signup_button: Button

    private var input_email: String = ""
    private var input_nickname: String = ""
    private var input_pw: String = ""
    private var input_pw_check: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_up)

        backButton = findViewById(R.id.back_button)
        backButton.setOnClickListener(this)

        // 이메일
        sign_email = findViewById(R.id.sign_email)
        email_error_message_textview = findViewById(R.id.email_error_message_textview)
        email_check_button = findViewById(R.id.email_check_button)
        email_check_button.setOnClickListener(this)

        // 닉네임


        // 뷰 가져오기
//        signup_back = findViewById(R.id.signup_back)
//        email_check_button = findViewById(R.id.email_check_button)
//        email_hint = findViewById(R.id.email_hint)
//        nickname_check_button = findViewById(R.id.nickname_check_button)
//        nickname_hint = findViewById(R.id.nickname_hint)
//        pw_hint = findViewById(R.id.pw_hint)
//        pw2_hint = findViewById(R.id.pw2_hint)
//        signup_button = findViewById(R.id.signup_button)

        // 버튼별 OnClickListener 등록
//        signup_back.setOnClickListener(this)
//        email_check_button.setOnClickListener(this)
//        nickname_check_button.setOnClickListener(this)
//        signup_button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.back_button -> {back()}
//            R.id.email_check_button -> {emailCheck()}
//            R.id.nickname_check_button -> {nicknameCheck()}
//            R.id.signup_button -> {signup()}
        }
    }

    private fun back() {
        onBackPressed()  // 뒤로 가기 버튼 동작 추가
    }

    private fun emailCheck() {
        input_email = sign_email.text.toString()
    }

    private fun nicknameCheck() {
        TODO("Not yet implemented")
    }

    private fun signup() {
        TODO("Not yet implemented")
    }

}