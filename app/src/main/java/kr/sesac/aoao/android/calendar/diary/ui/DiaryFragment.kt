package kr.sesac.aoao.android.calendar.diary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kr.sesac.aoao.android.calendar.diary.service.DiaryRepository
import kr.sesac.aoao.android.common.ToastGenerator
import kr.sesac.aoao.android.common.TokenManager
import kr.sesac.aoao.android.databinding.FragmentDiaryBinding
import kr.sesac.aoao.android.model.TodayViewModel

/**
 * @since 2024.01.22
 * @author 김유빈
 */
class DiaryFragment : Fragment() {

    private val diaryRepository = DiaryRepository

    private lateinit var binding : FragmentDiaryBinding
    private lateinit var todayViewModel: TodayViewModel

    private lateinit var accessToken: String

    private var isWritten = false
    private var selectedDate: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, // 뷰를 생성하는 객체
        container: ViewGroup?, // 생성할 뷰(자식 뷰)가 들어갈 부모 뷰
        savedInstanceState: Bundle? // 이전 프래그먼트 객체에서 전달된 데이터(번들)
    ): View {
        binding = FragmentDiaryBinding.inflate(inflater, container, false)
        todayViewModel = ViewModelProvider(requireActivity())[TodayViewModel::class.java]

        accessToken = TokenManager.getAccessTokenWithTokenType(requireContext())

        observeSelectedDate()
        setUpdateButtonClickEvent()
        setDeleteButtonClockEvent()
        return binding.root
    }

    /**
     * Calendar Fragment 에서 전달한 데이터 표출
     * @since 2024.01.22
     * @author 김유빈
     */
    private fun observeSelectedDate() {
        todayViewModel.today.observe(viewLifecycleOwner) { date ->
            val today = "${date.year} / ${date.month} / ${date.dayOfMonth}"
            binding.date.text = today

            selectedDate = "${date.year}-${date.month}-${date.dayOfMonth}"
            writeDiary()
        }
    }

    /**
     * 수정 버튼 이벤트 설정
     * @since 2024.01.22
     * @author 최정윤
     */
    private fun setUpdateButtonClickEvent() {
        binding.updateBtn.setOnClickListener {
            when (isWritten) {
                true -> {

                }
                false -> {
                    saveDiary(binding.diaryEditText.text.toString())
                    binding.updateBtn.text = "수정"
                    isWritten = false
                }
            }
        }
    }

    /**
     * 삭제 버튼 이벤트 설정
     * @since 2024.01.22
     * @author 최정윤
     */
    private fun setDeleteButtonClockEvent() {
        binding.deleteBtn.setOnClickListener {
            binding.diaryEditText.setText("")
            saveDiary("")
        }
    }

    /**
     * 다이어리 파일 읽어와 뷰에 표출
     * @since 2024.01.22
     * @author 최정윤
     *
     * 다이어리 조회 API 연결
     * @since 2024.01.28
     * @author 김유빈
     */
    private fun writeDiary() {
        diaryRepository.findByDate(accessToken, selectedDate, requireActivity(),
            onResponse = { response ->
                if (response.success && response.date != null) {
                    binding.diaryEditText.setText(response.date!!.content)
                    isWritten = true
                }
            },
            onFailure = {
                binding.diaryEditText.setText("")
            })
    }

    /**
     * 다이어리 파일 작성 기능
     * @since 2024.01.22
     * @author 최정윤
     *
     * 다이어리 수정 API 연결
     * @since 2024.01.28
     * @author 김유빈
     */
    private fun saveDiary(content: String) {
        diaryRepository.save(accessToken, selectedDate, content, requireActivity(),
            onResponse = { response ->
                if (response.success) {
                    writeDiary()
                }
            },
            onFailure = { response ->
                ToastGenerator.showShortToast(response.message, requireActivity())
            })
    }
}
