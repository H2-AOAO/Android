package kr.sesac.aoao.android

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kr.sesac.aoao.android.databinding.FragmentDiaryBinding
import kr.sesac.aoao.android.model.TodayViewModel
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream

class DiaryFragment : Fragment() {

    private lateinit var binding : FragmentDiaryBinding
    private lateinit var todayViewModel: TodayViewModel

    private lateinit var year: String
    private lateinit var month: String
    private lateinit var dayOfMonth: String
    private lateinit var fname: String

    private var isEdit = false
    private val userId = "userId"

    override fun onCreateView(
        inflater: LayoutInflater, // 뷰를 생성하는 객체
        container: ViewGroup?, // 생성할 뷰(자식 뷰)가 들어갈 부모 뷰
        savedInstanceState: Bundle? // 이전 프래그먼트 객체에서 전달된 데이터(번들)
    ): View {
        binding = FragmentDiaryBinding.inflate(inflater, container, false)
        todayViewModel = ViewModelProvider(requireActivity())[TodayViewModel::class.java]

        observeSelectedDate()
        setUpdateButtonClickEvent()
        setDeleteButtonClockEvent()
        return binding.root
    }

    private fun observeSelectedDate() {
        todayViewModel.year.observe(viewLifecycleOwner) { year ->
            todayViewModel.month.observe(viewLifecycleOwner) { month ->
                todayViewModel.dayOfMonth.observe(viewLifecycleOwner) { dayOfMonth ->
                    val today = "$year / $month / $dayOfMonth"
                    binding.date.text = today

                    this.year = year.toString()
                    this.month = month.toString()
                    this.dayOfMonth = dayOfMonth.toString()

                    writeDiary()
                }
            }
        }
    }

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

    private fun setDeleteButtonClockEvent() {
        binding.deleteBtn.setOnClickListener {
            binding.diaryEditText.setText("")
            saveDiary(fname, "")
        }
    }

    private fun writeDiary() {
        fname = "$userId$year-$month-$dayOfMonth.txt"
        val fileInputStream: FileInputStream
        try {
            fileInputStream = requireContext().openFileInput(fname)
            val fileData = ByteArray(fileInputStream.available())
            fileInputStream.read(fileData)
            fileInputStream.close()
            binding.diaryEditText.setText(String(fileData))
        } catch (e: FileNotFoundException) {
            binding.diaryEditText.setText("")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 달력 내용 추가
    @SuppressLint("WrongConstant")
    fun saveDiary(readDay: String?, content: String) {
        var fileOutputStream: FileOutputStream
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
