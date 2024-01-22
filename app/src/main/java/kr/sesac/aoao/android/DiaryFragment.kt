package kr.sesac.aoao.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kr.sesac.aoao.android.databinding.FragmentDiaryBinding
import kr.sesac.aoao.android.model.TodayViewModel

class DiaryFragment : Fragment() {

    private lateinit var binding : FragmentDiaryBinding
    private lateinit var todayViewModel: TodayViewModel

    override fun onCreateView(
        inflater: LayoutInflater, // 뷰를 생성하는 객체
        container: ViewGroup?, // 생성할 뷰(자식 뷰)가 들어갈 부모 뷰
        savedInstanceState: Bundle? // 이전 프래그먼트 객체에서 전달된 데이터(번들)
    ): View {
        binding = FragmentDiaryBinding.inflate(inflater, container, false)
        todayViewModel = ViewModelProvider(requireActivity())[TodayViewModel::class.java]
        observeSelectedDate()
        return binding.root
    }

    private fun observeSelectedDate() {
        todayViewModel.selectedDate.observe(viewLifecycleOwner) { today ->
            binding.date.text = today
        }
    }
}
