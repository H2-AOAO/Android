package kr.sesac.aoao.android.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TodoFolderData(var name: String, var todos: MutableList<TodoData>, var colorCode: String) :
    Parcelable
