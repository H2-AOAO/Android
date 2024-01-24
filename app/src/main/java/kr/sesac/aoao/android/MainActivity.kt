package kr.sesac.aoao.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.sesac.aoao.android.calendar.ui.HomeActivity
import kr.sesac.aoao.android.dino.ui.RaiseDinoActivity
import kr.sesac.aoao.android.friend.FriendActivity
import kr.sesac.aoao.android.user.ui.MyPageActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}