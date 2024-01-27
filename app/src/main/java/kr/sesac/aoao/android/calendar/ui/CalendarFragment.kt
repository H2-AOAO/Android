package kr.sesac.aoao.android.calendar.ui

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kr.sesac.aoao.android.databinding.FragmentCalendarBinding
import kr.sesac.aoao.android.model.TodayViewModel
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * @since 2024.01.22
 * @author 김유빈
 */
class CalendarFragment: Fragment() {

    private lateinit var binding : FragmentCalendarBinding
    private lateinit var todayViewModel: TodayViewModel

    override fun onCreateView(
        inflater: LayoutInflater, // 뷰를 생성하는 객체
        container: ViewGroup?, // 생성할 뷰(자식 뷰)가 들어갈 부모 뷰
        savedInstanceState: Bundle? // 이전 프래그먼트 객체에서 전달된 데이터(번들)
    ): View {
        binding = FragmentCalendarBinding.inflate(layoutInflater)
        todayViewModel = ViewModelProvider(requireActivity())[TodayViewModel::class.java]

        setTodayWhenStart()
        setOnDateChangeEvent()

        return binding.root
    }

    /**
     * 화면이 시작되었을 때 오늘 날짜 조회하여 date 날짜 설정
     * @since 2024.01.27
     * @author 김유빈
     */
    private fun setTodayWhenStart() {
        val today: Date = Calendar.getInstance().time
        val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        val monthFormat = SimpleDateFormat("MM", Locale.getDefault())
        val dayFormat = SimpleDateFormat("dd", Locale.getDefault())

        todayViewModel.onSelectionChanged(
            yearFormat.format(today),
            monthFormat.format(today),
            dayFormat.format(today)
        )
    }

    /**
     * 선택한 데이터 정보 ViewModel 담아 전달
     * @since 2024.01.22
     * @author 김유빈
     */
    private fun setOnDateChangeEvent() {
        val calendar: CalendarView = binding.calendarView

        calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            todayViewModel.onSelectionChanged(year.toString(), (month + 1).toString(), dayOfMonth.toString())
        }
    }
}
