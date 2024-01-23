package kr.sesac.aoao.android.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TodoData(var content: String, var checked: Boolean) : Parcelable
