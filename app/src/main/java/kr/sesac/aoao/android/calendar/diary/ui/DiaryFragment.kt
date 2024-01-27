package kr.sesac.aoao.android.calendar.diary.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kr.sesac.aoao.android.calendar.diary.service.DiaryRepository
import kr.sesac.aoao.android.common.ToastGenerator
import kr.sesac.aoao.android.common.TokenManager
import kr.sesac.aoao.android.databinding.FragmentDiaryBinding
import kr.sesac.aoao.android.model.TodayViewModel
import java.io.FileOutputStream

/**
 * @since 2024.01.22
 * @author 김유빈
 */
class DiaryFragment : Fragment() {

    private val diaryRepository = DiaryRepository

    private lateinit var binding : FragmentDiaryBinding
    private lateinit var todayViewModel: TodayViewModel

    private lateinit var fname: String
    private lateinit var accessToken: String

    private var isEdit = false

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

            writeDiary("${date.year}-${date.month}-${date.dayOfMonth}")
        }
    }

    /**
     * 수정 버튼 이벤트 설정
     * @since 2024.01.22
     * @author 최정윤
     */
    private fun setUpdateButtonClickEvent() {
        binding.updateBtn.setOnClickListener {
            if (isEdit) {
                isEdit = false
                binding.diaryEditText.isEnabled = false
                binding.updateBtn.text = "수정"
                saveDiary(fname, binding.diaryEditText.text.toString())
            } else {
                isEdit = true
                binding.diaryEditText.isEnabled = true
                binding.updateBtn.text = "저장"
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
            saveDiary(fname, "")
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
    private fun writeDiary(date: String) {
        diaryRepository.findByDate(accessToken, date, requireActivity(),
            onResponse = { response ->
                if (response.success && response.date != null) {
                    binding.diaryEditText.setText(response.date!!.content)
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
     */
    // 달력 내용 추가
    @SuppressLint("WrongConstant")
    fun saveDiary(readDay: String?, content: String) {
        val fileOutputStream: FileOutputStream
        try {
            fileOutputStream = requireContext().openFileOutput(readDay,
                AppCompatActivity.MODE_NO_LOCALIZED_COLLATORS
            )
            fileOutputStream.write(content.toByteArray())
            fileOutputStream.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}
