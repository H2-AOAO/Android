package kr.sesac.aoao.android

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kr.sesac.aoao.android.databinding.ActivityRaiseDinoBinding
import kr.sesac.aoao.android.model.ItemData


class RaiseDinoActivity : AppCompatActivity(){
    lateinit var binding : ActivityRaiseDinoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.white)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR //상태바 글자색 검은색으로
        }
        binding = ActivityRaiseDinoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val recyclerView = binding.recyclerView
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        val itemList = listOf(
            ItemData(num = 1, name = "item_love", price = 0),
            ItemData(num = 2, name = "item_food",price = 0),
            ItemData(num = 3, name = "item_medicine",price = 0),
            ItemData(num = 14, name = "item_play",price = 0)
        )
        val adapter = RecyclerViewAdapter_RaiseDino(itemList,this)
        recyclerView.adapter = adapter

        binding.btnGotoMarket.setOnClickListener {
            val intent = Intent(this, MarketActivity::class.java)
            startActivity(intent)
        }

    }

}