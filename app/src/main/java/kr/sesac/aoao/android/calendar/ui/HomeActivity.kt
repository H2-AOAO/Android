package kr.sesac.aoao.android.calendar.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kr.sesac.aoao.android.calendar.diary.ui.DiaryFragment
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.calendar.todo.ui.TodolistFragment
import kr.sesac.aoao.android.databinding.ActivityCalendarBinding
import kr.sesac.aoao.android.model.TodayViewModel
import kr.sesac.aoao.android.model.TodoData
import kr.sesac.aoao.android.model.TodoFolderData
import kr.sesac.aoao.android.model.TodoFoldersData
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
    private val folders = findTodoFolders()

    /**
     * 캘린더 구현
     * @since 2024.01.19
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
        switchFragment(contentLayoutId, createTodolistFragment())

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
                switchFragment(contentLayoutId, createTodolistFragment())
            }
        }
    }

    /**
     * 다른 액티비티로 데이터 전달
     * @since 2024.01.23
     * @author 김유빈
     */
    private fun createTodolistFragment(): TodolistFragment {
        val bundle = Bundle()
        bundle.putParcelable("folders", folders)
        val fragment = TodolistFragment()
        fragment.arguments = bundle
        return fragment
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
            val intent = Intent(this, TodoFolderActivity::class.java)
            intent.putExtra("folders", folders)
            startActivity(intent)
        }
    }

    /**
     * 투두리스트 정보 조회
     * @since 2024.01.23
     * @author 김유빈
     */
    private fun findTodoFolders(): TodoFoldersData {
        return TodoFoldersData(
            mutableListOf(
                TodoFolderData(
                    "공부", mutableListOf(
                        TodoData("토익 1시간", false),
                        TodoData("전공 서적 1회독", true),
                    ),
                    "blue"
                ),
                TodoFolderData(
                    "루틴", mutableListOf(
                        TodoData("토익 1시간", false),
                        TodoData("일기쓰기", true),
                        TodoData("운동하기", true),
                    ),
                    "pink"
                ),
                TodoFolderData(
                    "생활", mutableListOf(
                        TodoData("방청소하기", false),
                        TodoData("장보기", true),
                    ),
                    "yellow"
                ),
            )
        )
    }
}
