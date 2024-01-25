package kr.sesac.aoao.android.friend

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.*

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.common.BottomNavigationHandler

/**
 * @since 2024.01.24
 * @author 최정윤
 */
public class FriendActivity : AppCompatActivity() {
    private lateinit var searchEditText: EditText
    private lateinit var friendsListView: ListView
    private lateinit var titleTextView: TextView
    private lateinit var adapter: ArrayAdapter<String>

    private val dummyData = listOf(
        "김유빈", "김은서", "김은솔", "엄상은", "이상민", "이혜연", "황수연", "최정윤"
    )

    /**
     * 친구리스트 구현
     * @since 2024.01.24
     * @author 최정윤
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        BottomNavigationHandler(this, "FriendActivity").attach(bottomNavigationView)

        titleTextView = findViewById(R.id.titleTextView)
        searchEditText = findViewById(R.id.searchEditText)
        friendsListView = findViewById(R.id.friendsListView)

        // 어댑터 초기화 및 더미 데이터 설정
        adapter = object : ArrayAdapter<String>(this, R.layout.recycle_friend_item_list, R.id.friendNameTextView, dummyData) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val itemView = super.getView(position, convertView, parent)

                // 현재 위치에 있는 친구 이름 가져오기
                val currentFriendName = getItem(position)

                // 레이아웃 요소 초기화
                val profileImageView = itemView.findViewById<ImageView>(R.id.profileImageView)
                val friendNameTextView = itemView.findViewById<TextView>(R.id.friendNameTextView)
                val addButton = itemView.findViewById<Button>(R.id.addButton)

                // 데이터 설정 (프로필 이미지 리소스 ID와 클릭 리스너는 적절하게 수정해야 함)
                // 예제에서는 profile1, profile2, ... 와 같이 프로필 이미지 리소스를 사용
//                val profileImageResource = resources.getIdentifier("profile${position + 1}", "drawable", packageName)
                val profileImageResource = resources.getIdentifier("user_profile", "drawable", packageName)
                profileImageView.setImageResource(profileImageResource)
                friendNameTextView.text = currentFriendName

                // 추가 버튼 클릭 리스너 설정 (원하는 동작 추가)
                addButton.setOnClickListener {
                    // 추가 버튼 클릭 시 수행할 동작
                }

                return itemView
            }
        }

        friendsListView.adapter = adapter


        /**
         * 친구 검색기능 구현
         * @since 2024.01.24
         * @author 최정윤
         */
        // 검색 기능 구현
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable?) {}
        })
    }
}
