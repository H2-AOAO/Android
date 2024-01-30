package kr.sesac.aoao.android.common

import android.content.Context
import android.content.Intent
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.calendar.ui.HomeActivity
import kr.sesac.aoao.android.dino.ui.RaiseDinoActivity
import kr.sesac.aoao.android.friend.FriendActivity
import kr.sesac.aoao.android.user.ui.MyPageActivity

class BottomNavigationHandler(private val context: Context, private val activityName : String) {

    fun attach(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    if(!activityName.equals("HomeActivity")) startActivity(HomeActivity::class.java)
                     true
                }
                R.id.navigation_dino -> {
                    if(!activityName.equals("DinoActivity")) startActivity(RaiseDinoActivity::class.java)
                    true
                }
                R.id.navigation_mypage -> {
                    if(!activityName.equals("MypageActivity")) startActivity(MyPageActivity::class.java)
                    true
                }
                else -> false
            }
        }
    }

    private fun startActivity(clazz: Class<*>) {
        context.startActivity(Intent(context, clazz))
    }
}