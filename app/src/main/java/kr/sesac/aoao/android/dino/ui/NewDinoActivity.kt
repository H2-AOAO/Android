package kr.sesac.aoao.android.dino.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.common.ToastGenerator
import kr.sesac.aoao.android.common.TokenManager
import kr.sesac.aoao.android.databinding.ActivityNewDinoBinding
import kr.sesac.aoao.android.dino.model.request.NewDinoRequest
import kr.sesac.aoao.android.dino.service.NewDinoUtil

/**
 * @since 2024.01.24
 * @author 김은서
 */
class NewDinoActivity : AppCompatActivity(){
    lateinit var binding: ActivityNewDinoBinding
    var dinoColor : String = "green"
    lateinit var dinoName: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white) //상태바 색깔 하얀색
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR //상태바 글자색 검은색
        binding = ActivityNewDinoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val accessToken = TokenManager.getAccessTokenWithTokenType(this)

        dinoName = binding.dinoNameInput;
        //사용자가 선택한 색에 맞추어 색 업데이트 및 UI 적용
        binding.btnPink.setOnClickListener { checkColor("pink") }
        binding.btnBlue.setOnClickListener { checkColor("blue") }
        binding.btnYellow.setOnClickListener { checkColor("yellow") }
        binding.btnPurple.setOnClickListener { checkColor("purple") }
        binding.btnGreen.setOnClickListener { checkColor("green") }

        binding.btnSetDino.setOnClickListener {
            saveDino(accessToken)
        }
    }

    /**
     * 선택한 색 업데이트
     * @since 2024.01.23
     * @author 김은서
     */
    private fun checkColor(color : String) {
        val colorList : List<String> = listOf(
            "pink", "blue","yellow","purple","green"
        )
        dinoColor = color
        for(i in 0..4){
            val viewName = "${colorList[i]}_check"
            val resourceId = resources.getIdentifier(viewName, "id", packageName)
            if (colorList[i].equals(color)) findViewById<View>(resourceId)?.visibility = View.VISIBLE //선택한 색이라면 체크 표시 활성화
            else findViewById<View>(resourceId)?.visibility = View.INVISIBLE //선택하지 않은 색들 체크 표시 비활성화
        }
    }

    /**
     * 다이노 생성 통신
     * @since 2024.01.26
     * @author 김은서
     */
    private fun saveDino(accessToken : String){
        val newDinoRequest = NewDinoRequest(dinoColor, dinoName.getText().toString())
        NewDinoUtil.newDino(accessToken,
                            newDinoRequest,
                            this@NewDinoActivity,
            onResponse = {
                response ->
                if(response.success){
                    ToastGenerator.showShortToast("다이노 생성 완료!", this);
                    val intent = Intent(this, RaiseDinoActivity::class.java)
                    startActivity(intent);
                    finish()
                }
            },
            onFailure = {
                ToastGenerator.showShortToast("서버 오류 발생. 다시 시도해주세요",this);
            }
        )
    }
}