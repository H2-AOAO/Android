package kr.sesac.aoao.android.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TodayViewModel : ViewModel() {

    val selectedDate = MutableLiveData<String>()
}