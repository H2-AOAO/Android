package kr.sesac.aoao.android.dino.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.databinding.ActivityNewDinoBinding
import kr.sesac.aoao.android.databinding.ActivityRaiseDinoBinding
import kr.sesac.aoao.android.market.ui.MarketActivity

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.white) //상태바 색깔 하얀색
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR //상태바 글자색 검은색
        }
        binding = ActivityNewDinoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dinoName = findViewById(R.id.dino_name_input);

        //사용자가 선택한 색에 맞추어 색 업데이트 및 UI 적용
        binding.btnPink.setOnClickListener { checkColor("pink") }
        binding.btnBlue.setOnClickListener { checkColor("blue") }
        binding.btnYellow.setOnClickListener { checkColor("yellow") }
        binding.btnPurple.setOnClickListener { checkColor("purple") }
        binding.btnGreen.setOnClickListener { checkColor("green") }

        binding.btnSetDino.setOnClickListener {
            //다이노 업데이트 통신 서비스
            Log.d("응답-다이노", "$dinoColor,${dinoName.getText().toString()}")

            val intent = Intent(this, RaiseDinoActivity::class.java)
            startActivity(intent)
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
}