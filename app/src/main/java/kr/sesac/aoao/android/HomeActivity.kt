package kr.sesac.aoao.android

import android.annotation.SuppressLint
import java.io.FileInputStream
import java.io.FileOutputStream

import android.view.View
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

/**
 * @since 2024.01.19 ~
 * @author 김유빈, 최정윤
 */
class HomeActivity : AppCompatActivity(){

    var userID: String = "userID"
    lateinit var fname: String
    lateinit var str: String
    lateinit var calendarView: CalendarView
    lateinit var updateBtn: Button
    lateinit var deleteBtn:Button
    lateinit var saveBtn:Button
    lateinit var diaryTextView: TextView
    lateinit var diaryContent:TextView
    lateinit var title:TextView
    lateinit var contextEditText: EditText

    /**
     * 캘린더 구현
     * @since 2024.01.19
     * @author 최정윤
     *
     * 화면 스위칭 구현
     * @since 2024.01.22
     * @author 최정윤
     *
     * 캘린더 디자인 변경
     * @since 2024.01.22
     * @author 최정윤
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        // 스위칭 객체 생성
        val statusText: TextView = findViewById(R.id.todo)
        val switchView: SwitchCompat = findViewById(R.id.diary)

        //switch 체크 이벤트
        switchView.setOnCheckedChangeListener { p0, isChecked ->
            if (isChecked) {
                statusText.text = "다이어리"
                showDiaryFunctionality()
                isDiarySwitched = true
            } else {
                statusText.text = "투두"
                showTodoFunctionality()
                isDiarySwitched = false
            }

            // checkDay 함수 호출로 인해 화면 업데이트
//            checkDay(year, month, dayOfMonth, userID)
        }

        // UI값 생성
        calendarView=findViewById(R.id.calendarView)
        diaryTextView=findViewById(R.id.diaryTextView)
        saveBtn=findViewById(R.id.saveBtn)
        deleteBtn=findViewById(R.id.deleteBtn)
        updateBtn=findViewById(R.id.updateBtn)
        diaryContent=findViewById(R.id.diaryContent)
//        title=findViewById(R.id.title)
        contextEditText=findViewById(R.id.contextEditText)

//        title.text = "달력 일기장"

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            diaryTextView.visibility = View.VISIBLE
            saveBtn.visibility = View.VISIBLE
            contextEditText.visibility = View.VISIBLE
            diaryContent.visibility = View.INVISIBLE
            updateBtn.visibility = View.INVISIBLE
            deleteBtn.visibility = View.INVISIBLE
            diaryTextView.text = String.format("%d / %d / %d", year, month + 1, dayOfMonth)
            contextEditText.setText("")
            checkDay(year, month, dayOfMonth, userID)
        }

        saveBtn.setOnClickListener {
            saveDiary(fname)
            contextEditText.visibility = View.INVISIBLE
            saveBtn.visibility = View.INVISIBLE
            updateBtn.visibility = View.VISIBLE
            deleteBtn.visibility = View.VISIBLE
            str = contextEditText.text.toString()
            diaryContent.text = str
            diaryContent.visibility = View.VISIBLE
        }
    }

    // 스위칭 상태를 나타내는 변수 추가
    private var isDiarySwitched = false // 기본적으로 투두 상태로 시작

    // 달력 내용 조회, 수정
    fun checkDay(cYear: Int, cMonth: Int, cDay: Int, userID: String) {

        // 가시성 체크를 try-catch 블록 외부로 이동
        var diaryVisibility = View.INVISIBLE
        var todoVisibility = View.INVISIBLE

        if (isDiarySwitched) {
            // 다이어리 스위칭 상태일 때의 처리
            showDiaryFunctionality()
            diaryVisibility = View.VISIBLE
        } else {
            // 투두 스위칭 상태일 때의 처리
            showTodoFunctionality()
            todoVisibility = View.VISIBLE
        }

        //저장할 파일 이름설정
        fname = "" + userID + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt"

        var fileInputStream: FileInputStream
        try {
            fileInputStream = openFileInput(fname)
            val fileData = ByteArray(fileInputStream.available())
            fileInputStream.read(fileData)
            fileInputStream.close()
            str = String(fileData)
            contextEditText.visibility = View.INVISIBLE
            diaryContent.visibility = diaryVisibility
            diaryContent.text = str
            saveBtn.visibility = View.INVISIBLE
            updateBtn.visibility = diaryVisibility
            deleteBtn.visibility = diaryVisibility
            updateBtn.setOnClickListener {
                contextEditText.visibility = View.VISIBLE
                diaryContent.visibility = View.INVISIBLE
                contextEditText.setText(str)
                saveBtn.visibility = View.VISIBLE
                updateBtn.visibility = View.INVISIBLE
                deleteBtn.visibility = View.INVISIBLE
                diaryContent.text = contextEditText.text
            }
            deleteBtn.setOnClickListener {
                diaryContent.visibility = View.INVISIBLE
                updateBtn.visibility = View.INVISIBLE
                deleteBtn.visibility = View.INVISIBLE
                contextEditText.setText("")
                contextEditText.visibility = View.VISIBLE
                saveBtn.visibility = View.VISIBLE
                removeDiary(fname)
            }
            if (diaryContent.text == null) {
                diaryContent.visibility = View.INVISIBLE
                updateBtn.visibility = View.INVISIBLE
                deleteBtn.visibility = View.INVISIBLE
                diaryTextView.visibility = View.VISIBLE
                saveBtn.visibility = View.VISIBLE
                contextEditText.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 달력 내용 제거
    @SuppressLint("WrongConstant")
    fun removeDiary(readDay: String?) {
        var fileOutputStream: FileOutputStream
        try {
            fileOutputStream = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS)
            val content = ""
            fileOutputStream.write(content.toByteArray())
            fileOutputStream.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    // 달력 내용 추가
    @SuppressLint("WrongConstant")
    fun saveDiary(readDay: String?) {
        var fileOutputStream: FileOutputStream
        try {
            fileOutputStream = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS)
            val content = contextEditText.text.toString()
            fileOutputStream.write(content.toByteArray())
            fileOutputStream.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    //    스위칭 다이어리 보이게 하기
    private fun showDiaryFunctionality() {
        // 다이어리 관련 기능 보이게 설정
        diaryTextView.visibility = View.VISIBLE
        saveBtn.visibility = View.VISIBLE
        contextEditText.visibility = View.VISIBLE
        diaryContent.visibility = View.INVISIBLE
        updateBtn.visibility = View.INVISIBLE
        deleteBtn.visibility = View.INVISIBLE

        // 투두 관련 기능 숨기기
        // TODO: 투두 관련 기능에 대한 가시성 조절 코드 추가
    }

    //    스위칭 투두 보이게 하기
    private fun showTodoFunctionality() {
        // 투두 관련 기능 보이게 설정
        // TODO: 투두 관련 기능에 대한 가시성 조절 코드 추가

        // 다이어리 관련 기능 숨기기
        diaryTextView.visibility = View.INVISIBLE
        saveBtn.visibility = View.INVISIBLE
        contextEditText.visibility = View.INVISIBLE
        diaryContent.visibility = View.INVISIBLE
        updateBtn.visibility = View.INVISIBLE
        deleteBtn.visibility = View.INVISIBLE
    }

}