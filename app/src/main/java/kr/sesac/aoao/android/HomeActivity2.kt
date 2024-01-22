package kr.sesac.aoao.android

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kr.sesac.aoao.android.databinding.ActivityCalendar2Binding
import kr.sesac.aoao.android.model.TodayViewModel

/**
 * @since 2024.01.19 ~
 * @author 김유빈, 최정윤
 */
class HomeActivity2 : AppCompatActivity() {

    private lateinit var binding : ActivityCalendar2Binding
    private lateinit var todayViewModel: TodayViewModel

    private val calendarLayoutId = R.id.calendar
    private val contentLayoutId = R.id.content

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendar2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        todayViewModel = ViewModelProvider(this).get(TodayViewModel::class.java)

        // calendar 초기화
        switchFragment(calendarLayoutId, CalendarFragment())

        // switch 체크 이벤트
        setOnCheckedEvent()
        switchFragment(contentLayoutId, TodolistFragment())
    }

    private fun setOnCheckedEvent(
    ) {
        val contentTitle: TextView = binding.todo
        val switchingButton: SwitchCompat = binding.switchingButton
        switchingButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                contentTitle.text = "다이어리"
                switchFragment(contentLayoutId, DiaryFragment())
            } else {
                contentTitle.text = "투두"
                switchFragment(contentLayoutId, TodolistFragment())
            }
        }
    }

    private fun switchFragment(layoutId: Int, fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(layoutId, fragment)
        fragmentTransaction.commit()
    }
}
