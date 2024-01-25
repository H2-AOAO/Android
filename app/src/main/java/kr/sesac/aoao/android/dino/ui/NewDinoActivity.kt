package kr.sesac.aoao.android.dino.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.util.Log
import android.view.View
import android.widget.Button
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
    var dinoName : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.white) //상태바 색깔 하얀색
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR //상태바 글자색 검은색
        }
        binding = ActivityNewDinoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnPink.setOnClickListener { checkColor("pink") }
        binding.btnBlue.setOnClickListener { checkColor("blue") }
        binding.btnYellow.setOnClickListener { checkColor("yellow") }
        binding.btnPurple.setOnClickListener { checkColor("purple") }
        binding.btnGreen.setOnClickListener { checkColor("green") }

        binding.btnSetDino.setOnClickListener {
            //다이노 업데이트 통신 서비스

            val intent = Intent(this, RaiseDinoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkColor(color : String) {
        val colorList : List<String> = listOf(
            "pink", "blue","yellow","purple","green"
        )
        dinoColor = color
        for(i in 0..4){
            val viewName = "${colorList[i]}_check"
            val resourceId = resources.getIdentifier(viewName, "id", packageName)
            if (colorList[i].equals(color)) findViewById<View>(resourceId)?.visibility = View.VISIBLE
             else findViewById<View>(resourceId)?.visibility = View.INVISIBLE
        }
    }
}