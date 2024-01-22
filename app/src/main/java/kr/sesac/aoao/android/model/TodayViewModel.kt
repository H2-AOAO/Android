package kr.sesac.aoao.android.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TodayViewModel : ViewModel() {

    val year = MutableLiveData<Int>()
    val month = MutableLiveData<Int>()
    val dayOfMonth = MutableLiveData<Int>()
}