package kr.sesac.aoao.android.user.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import java.net.URL
import java.util.concurrent.Executors

/**
 * url로 이미지 불러오기
 *
 * @since 2024.01.28
 * @author 이상민
 */
object ImageLoader {
    fun load(url : String, view : ImageView){
        val executors = Executors.newSingleThreadExecutor()
        var image : Bitmap? = null
        executors.execute {
            try {
                image = BitmapFactory.decodeStream(URL(url).openStream())
                view.setImageBitmap(image)
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
    }
}