package kr.sesac.aoao.android.user.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.sesac.aoao.android.MainActivity
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.common.BottomNavigationHandler
import kr.sesac.aoao.android.common.TokenManager
import kr.sesac.aoao.android.databinding.ActivityMyPageBinding
import kr.sesac.aoao.android.dino.service.DinoInfoUtil
import kr.sesac.aoao.android.user.service.UserRepository
import kr.sesac.aoao.android.user.utils.FilePicker
import kr.sesac.aoao.android.user.utils.FileUploader
import kr.sesac.aoao.android.user.utils.ImageLoader
import kotlin.properties.Delegates

/**
 * @since 2024.01.23
 * @author 이상민
 */
class MyPageActivity : AppCompatActivity() {

    private val userRepository = UserRepository

    private lateinit var accessToken: String
    private var userId by Delegates.notNull<Long>()

    private lateinit var binding: ActivityMyPageBinding

    private lateinit var userImg : ImageView
    private lateinit var nickname : TextView
    private lateinit var email : TextView
    private lateinit var profileEditButton : Button
    private lateinit var pwEditButton : Button

    // 투두
    private lateinit var monthEditTextView : TextView
    private lateinit var monthText : TextView
    private lateinit var finishEditTextView : TextView
    private lateinit var todayEditTextView : TextView


    private lateinit var dinoName : TextView
    private lateinit var dinoImg : ImageView

    private lateinit var logOut : Button

    private fun initializeViews() {
        userImg = binding.userImg
        nickname = binding.nickname
        email = binding.email
        profileEditButton = binding.profileEditButton
        monthEditTextView = binding.monthEditTextView // 이번달 총 투두
        monthText = binding.monthText
        finishEditTextView = binding.finishEditTextView
        todayEditTextView = binding.todayEditTextView

        dinoName = binding.mypageDinoName
        dinoImg = binding.mypageDinoImg

        pwEditButton = binding.profileEditButton
        pwEditButton.setOnClickListener{
            val intent = Intent(this@MyPageActivity, ProfileEditActivity::class.java)
            startActivity(intent)
            finish()
        }

        logOut = binding.logOut

//        userImg.setOnClickListener{
//            // 파일 선택
//            FilePicker.pickFile(this)
//        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white) //상태바 색깔 하얀색
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR //상태바 글자색 검은색
        binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeViews()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        BottomNavigationHandler(this,"MypageActivity").attach(bottomNavigationView)


        accessToken = TokenManager.getAccessTokenWithTokenType(this)
        userId = TokenManager.getUserId(this).toLong()
        getMypage()
        getDinoInfo()

        binding.logOut.setOnClickListener {

            logout()

            val intent = Intent(this@MyPageActivity, MainActivity::class.java )
            startActivity(intent)
            finishAffinity();
        }
    }

    /**
     * 마이 페이지 API 호출
     * @since 2024.01.27
     * @author 이상민
     */
    private fun getMypage() {
        userRepository.getMypage(accessToken, userId, this@MyPageActivity,
            onResponse = { response ->
                if (response.success && response.date != null) {
                    val result = response.date!!
                    nickname.text = result.nickname
                    email.text = result.email
                    monthText.text = result.month
                    monthEditTextView.text = result.monthSumTodo.toString()
                    finishEditTextView.text = result.sumTodo.toString()
                    todayEditTextView.text = result.today

                    if(response.date!!.profile != "no_profile"){
                        ImageLoader.load(response.date!!.profile, userImg)
                    }
                }
            },
            onFailure = {
            })
    }


    /**
     * 다이노 정보 조회 호출
     * @since 2024.01.28
     * @author 김은서
     */
    private fun getDinoInfo(){ //서버로부터 다이노 정보 가져오기
        DinoInfoUtil.getDinoInfo(accessToken, this,
            onResponse = {
                    response ->
                if(response.success){
                    val dinoResponse = response.date!! //데이터값 받은 후, UI 변경
                    var dinoImgName = "dino_${dinoResponse.color}_lv${dinoResponse.lv}"
                    if(dinoResponse.lv == 1) dinoImgName = "dino_egg"
                    val resId = resources.getIdentifier(dinoImgName, "drawable", packageName)
                    dinoImg.setImageResource(resId)
                    dinoName.text = dinoResponse.name
                }
                else dinoName.text = "아직 다이노가 없어요!"
            },
            onFailure = {dinoName.text = "아직 다이노가 없어요!"}
        )
    }

    /**
     * logout api 호출
     * @since 2024.01.27
     * @author 이상민
     */
    private fun logout(){
        userRepository.logout(accessToken, this,
            onResponse = {
                    response ->
                if(response.success){

                }
            },
            onFailure = {

            }
        )
    }
}
