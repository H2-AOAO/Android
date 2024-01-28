package kr.sesac.aoao.android.calendar.diary.ui

import android.os.Bundle
import android.util.Log
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
    private var diaryId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, // 뷰를 생성하는 객체
        container: ViewGroup?, // 생성할 뷰(자식 뷰)가 들어갈 부모 뷰
        savedInstanceState: Bundle? // 이전 프래그먼트 객체에서 전달된 데이터(번들)
    ): View {
        binding = FragmentDiaryBinding.inflate(inflater, container, false)
        todayViewModel = ViewModelProvider(requireActivity())[TodayViewModel::class.java]

        accessToken = TokenManager.getAccessTokenWithTokenType(requireContext())

        observeSelectedDate()
        setUpWiteButtonClickEvent()
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
     * 에디트 텍스트 활성화
     * @since 2024.01.28
     * @author 김은서
     */
    private fun setEditTest(flag : Boolean){
        binding.diaryEditText.isFocusable = flag
        binding.diaryEditText.isFocusableInTouchMode = flag
        binding.diaryEditText.requestFocus()
    }

    /**
     * 다이어리 작성 활성화 이벤트 처리
     * @since 2024.01.28
     * @author 김은서
     */
    private fun setUpWiteButtonClickEvent() {
        binding.writeBtn.setOnClickListener {
            setEditTest(true)
        }
    }

    /**
     * 수정 버튼 이벤트 설정
     * @since 2024.01.22
     * @author 최정윤
     */
    private fun setUpdateButtonClickEvent() {
        binding.updateBtn.setOnClickListener {
            setEditTest(false)
            when (isWritten) { //수정의 경우 -> 업데이트
                true -> {
                    updateDiary(diaryId, binding.diaryEditText.text.toString())
                }
                false -> { //처음 저장 -> 포스트
                    saveDiary(binding.diaryEditText.text.toString())
                    isWritten = true
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
            setEditTest(false)
            deleteDiary(diaryId)
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
                if (response.success) {
                    binding.diaryEditText.setText(response.date!!.content)
                    diaryId = response.date!!.diaryId

                    isWritten = true
                }else {
                    isWritten = false
                }
            },
            onFailure = {
                binding.diaryEditText.setText("")
                isWritten = false
            })
    }

    /**
     * 다이어리 파일 작성 기능
     * @since 2024.01.22
     * @author 최정윤
     *
     * 다이어리 저장 API 연결
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

    /**
     * 다이어리 수정 API 연결
     * @since 2024.01.28
     * @author 김유빈
     */
    private fun updateDiary(diaryId: Long?, content: String) {
        diaryRepository.update(accessToken, diaryId, content, requireActivity(),
            onResponse = { response ->
                if (response.success) {
                    binding.diaryEditText.setText(content)
                }
            },
            onFailure = { response ->
                binding.diaryEditText.setText("")
                ToastGenerator.showShortToast(response.message, requireActivity())
            })
    }

    /**
     * 다이어리 삭제 API 연결
     * @since 2024.01.28
     * @author 김유빈
     */
    private fun deleteDiary(diaryId: Long?) {
        diaryRepository.delete(accessToken, diaryId, requireActivity(),
            onResponse = { response ->
                if (response.success) {
                    binding.diaryEditText.setText("")
                    isWritten = false
                }
            },
            onFailure = { response ->
                ToastGenerator.showShortToast(response.message, requireActivity())
            })
    }
}
