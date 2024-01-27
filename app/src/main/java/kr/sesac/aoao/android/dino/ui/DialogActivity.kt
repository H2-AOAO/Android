package kr.sesac.aoao.android.dino.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import kr.sesac.aoao.android.R

class DialogActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_dino_lv5)

        setGIF("firework_1", "firework_1")
        setGIF("firework_2", "firework_1")
        setGIF("firework_3", "firework_3")
        setGIF("firework_4", "firework_3")

        val dinoView = findViewById<ImageView>(R.id.final_dragon)
        val color = intent.getStringExtra("color") ?: "green"
        val dinoImgId = resources.getIdentifier("dino_${color}_lv4", "drawable", packageName)
        dinoView.setImageResource(dinoImgId)


        // AlertDialog를 사용하여 확인 버튼 추가
        val builder = AlertDialog.Builder(this)
        val btn : Button = findViewById(R.id.button)
        btn.setOnClickListener {
            val intent = Intent(this, NewDinoActivity::class.java)
            startActivity(intent)
        }

        val dialog = builder.create()
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.show()
    }
    fun setGIF(imageView:String , my_gif : String){
        val imageViewResId = resources.getIdentifier(imageView, "id", packageName)
        val imageView = findViewById<ImageView>(imageViewResId)
        val resourceId = resources.getIdentifier(my_gif, "raw", packageName)
        Glide.with(this).load(resourceId).into(imageView)
    }
}
