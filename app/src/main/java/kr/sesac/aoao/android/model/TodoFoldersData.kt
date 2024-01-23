package kr.sesac.aoao.android.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TodoFoldersData(val data: MutableList<TodoFolderData>) : Parcelable
