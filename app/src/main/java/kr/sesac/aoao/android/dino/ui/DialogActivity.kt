package kr.sesac.aoao.android.dino.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.databinding.DialogDinoLv5Binding
/**
 * 공룡 완성 축하용 다이얼로그
 * @since 2024.01.27
 * @author 김은서
 */
class DialogActivity : Activity() {
    private lateinit var binding: DialogDinoLv5Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogDinoLv5Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setGIF(binding.firework1, "firework_1")
        setGIF(binding.firework2, "firework_1")
        setGIF(binding.firework3, "firework_3")
        setGIF(binding.firework4, "firework_3")

        val color = intent.getStringExtra("color") ?: "green"
        val dinoImgId = resources.getIdentifier("dino_${color}_lv4", "drawable", packageName)
        binding.finalDragon.setImageResource(dinoImgId)

        // AlertDialog를 사용하여 확인 버튼 추가
        val builder = AlertDialog.Builder(this)
        binding.button.setOnClickListener {
            val intent = Intent(this, NewDinoActivity::class.java)
            startActivity(intent)
        }

        val dialog = builder.create()
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.show()
    }

    /**
     * gif  이미지뷰에 적용
     * @since 2024.01.27
     * @author 김은서
     */
    private fun setGIF(imageView: ImageView, my_gif: String) {
        val resourceId = resources.getIdentifier(my_gif, "raw", packageName)
        Glide.with(this).load(resourceId).into(imageView)
    }
}
