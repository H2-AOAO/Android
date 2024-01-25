package kr.sesac.aoao.android.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @since 2024.01.22
 * @author 김유빈
 */
class TodayViewModel : ViewModel() {

    private val _today = MutableLiveData<TodayData>()

    val today: LiveData<TodayData>
        get() = _today

    fun onSelectionChanged(year: Int, month: Int, dayOfMonth: Int) {
        val newToday = TodayData(year, month, dayOfMonth)
        _today.value = newToday
    }
}
