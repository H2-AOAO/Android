package kr.sesac.aoao.android.calendar.ui

import kr.sesac.aoao.android.common.BottomNavigationHandler
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kr.sesac.aoao.android.calendar.diary.ui.DiaryFragment
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.calendar.todo.ui.TodolistFragment
import kr.sesac.aoao.android.databinding.ActivityCalendarBinding
import kr.sesac.aoao.android.model.TodayViewModel
import kr.sesac.aoao.android.todofolder.ui.TodoFolderActivity

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
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        BottomNavigationHandler(this, "HomeActivity").attach(bottomNavigationView)

        todayViewModel = ViewModelProvider(this)[TodayViewModel::class.java]

        window.statusBarColor = ContextCompat.getColor(this, R.color.calendar)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR //상태바 글자색 검은색으로

        // calendar 초기화
        switchFragment(calendarLayoutId, CalendarFragment())

        // switch 체크 이벤트
        setOnCheckedEvent()
        switchFragment(contentLayoutId, TodolistFragment())

        // 목표 수정 버튼 클릭 이벤트
        setUpdateTodoFolderButtonOnClickEvent()
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
                binding.updateTodoFolderButton.visibility = View.INVISIBLE
                switchFragment(contentLayoutId, DiaryFragment())
            } else {
                contentTitle.text = "투두"
                binding.updateTodoFolderButton.visibility = View.VISIBLE
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

    /**
     * 목표 수정 클릭 시 폴더 목록 화면으로 전환
     * @since 2024.01.23
     * @author 김유빈
     */
    private fun setUpdateTodoFolderButtonOnClickEvent() {
        binding.updateTodoFolderButton.setOnClickListener {
            todayViewModel.today.observe(this) { date ->
                val today = "${date.year}-${date.month}-${date.dayOfMonth}"
                val intent = Intent(this, TodoFolderActivity::class.java)
                intent.putExtra("date", today)
                startActivity(intent)
            }
        }
    }
}
