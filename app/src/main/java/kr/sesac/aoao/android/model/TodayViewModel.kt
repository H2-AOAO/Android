package kr.sesac.aoao.android.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @since 2024.01.22
 * @author 김유빈
 */
class TodayViewModel : ViewModel() {

    val year = MutableLiveData<Int>()
    val month = MutableLiveData<Int>()
    val dayOfMonth = MutableLiveData<Int>()
}