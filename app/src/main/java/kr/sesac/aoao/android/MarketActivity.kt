package kr.sesac.aoao.android

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kr.sesac.aoao.android.databinding.ActivityMarketBinding
import kr.sesac.aoao.android.databinding.ActivityRaiseDinoBinding
import kr.sesac.aoao.android.model.ItemData

class MarketActivity : AppCompatActivity(){
    lateinit var binding : ActivityMarketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.raise_dino_light_green)
        }
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        binding = ActivityMarketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView = binding.recyclerView

        val itemList = listOf(
            ItemData(num = 0, name = "item_love", price = 20),
            ItemData(num = 0, name = "item_food",price = 30),
            ItemData(num = 0, name = "item_medicine",price = 30),
            ItemData(num = 0, name = "item_play",price = 40),
            ItemData(num = 0, name = "item_set",price = 100)
        )
        val adapter = RecyclerViewAdapter_Market(itemList,this)
        recyclerView.adapter = adapter

        binding.btnGotoHome.setOnClickListener {
            val intent = Intent(this, RaiseDinoActivity::class.java)
            startActivity(intent)
        }

    }
}