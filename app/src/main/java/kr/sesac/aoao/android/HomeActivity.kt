package kr.sesac.aoao.android

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kr.sesac.aoao.android.databinding.ActivityCalendarBinding
import kr.sesac.aoao.android.model.TodayViewModel

/**
 * @since 2024.01.19 ~
 * @author 김유빈, 최정윤
 */
class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCalendarBinding
    private lateinit var todayViewModel: TodayViewModel

    private val calendarLayoutId = R.id.calendar
    private val contentLayoutId = R.id.content

    /**
     * 캘린더 구현
     * @since 2024.01.19
     * @author 최정윤
     *
     * 캘린더 디자인 변경
     * @since 2024.01.22
     * @author 최정윤
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        todayViewModel = ViewModelProvider(this)[TodayViewModel::class.java]

        // calendar 초기화
        switchFragment(calendarLayoutId, CalendarFragment())

        // switch 체크 이벤트
        setOnCheckedEvent()
        switchFragment(contentLayoutId, TodolistFragment())
    }

    /**
     * 화면 스위칭 구현
     * @since 2024.01.22
     * @author 최정윤
     */
    private fun setOnCheckedEvent(
    ) {
        val contentTitle: TextView = binding.todo
        val switchingButton: SwitchCompat = binding.switchingButton
        switchingButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                contentTitle.text = "다이어리"
                binding.updateFolder.visibility = View.INVISIBLE
                switchFragment(contentLayoutId, DiaryFragment())
            } else {
                contentTitle.text = "투두"
                binding.updateFolder.visibility = View.VISIBLE
                switchFragment(contentLayoutId, TodolistFragment())
            }
        }
    }

    /**
     * 투두리스트 및 다이어리 fragment 스위칭 방식으로 변경
     * @since 2024.01.22
     * @author 김유빈
     */
    private fun switchFragment(layoutId: Int, fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(layoutId, fragment)
        fragmentTransaction.commit()
    }
}
