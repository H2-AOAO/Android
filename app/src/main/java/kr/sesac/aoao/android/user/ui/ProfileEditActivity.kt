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
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.common.TokenManager
import kr.sesac.aoao.android.databinding.ActivityPasswordEditBinding
import kr.sesac.aoao.android.databinding.ActivityProfileEditBinding
import kr.sesac.aoao.android.user.service.UserRepository
import kr.sesac.aoao.android.user.utils.FilePicker
import kr.sesac.aoao.android.user.utils.FileUploader
import kr.sesac.aoao.android.user.utils.NicknameHandler

/**
 * 프로필 편집
 * - 프로필 수정
 * - 닉네임변경 페이지 이동
 * - 비밀번호 변경 페이지로 이동
 *
 * @since 2024.01.28
 * @author 이상민
 */
class ProfileEditActivity : AppCompatActivity(){

    private lateinit var binding: ActivityProfileEditBinding

    private lateinit var accessToken: String

    private lateinit var backButton : ImageView

    // 프로필 사진
    private lateinit var imgUser : ImageView

    // 닉네임 변경 버튼
    private lateinit var changeNicknameButton : Button

    // 비밀번호 수정 버튼
    private lateinit var pwChangeButton : Button

    private fun initializeViews() {
        backButton = binding.backButton
        imgUser = binding.changeImgUser
        changeNicknameButton = binding.changeNicknameButton
        pwChangeButton = binding.pwChangeButton
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        accessToken = TokenManager.getAccessTokenWithTokenType(this)
        initializeViews()

        backButton.setOnClickListener{
            onBackPressed()
        }
        imgUser.setOnClickListener{
            FilePicker.pickFile(this) // 파일 선택
        }
        changeNicknameButton.setOnClickListener{
            val intent = Intent(this@ProfileEditActivity, NicknameActivity::class.java)
            startActivity(intent)
            finish()
        }
        pwChangeButton.setOnClickListener{
            val intent = Intent(this@ProfileEditActivity, PasswordEditActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // onActivityResult에서 처리
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        FilePicker.handleActivityResult(requestCode, resultCode, data,
            onSuccess = { selectedFileUri ->
                // 파일 업로드
                FileUploader.uploadFile(this, accessToken, selectedFileUri,
                    onSuccess = {
                        Log.d("MainActivity", "파일 업로드 성공")

                        val intent = Intent(this@ProfileEditActivity, MyPageActivity::class.java)
                        startActivity(intent)
                        finish()
                    },
                    onError = { errorMessage ->
                        Log.e("MainActivity", errorMessage)
                    }
                )
            },
            onError = {
                Log.e("MainActivity", "선택된 파일이 없습니다.")
            }
        )
    }
}
