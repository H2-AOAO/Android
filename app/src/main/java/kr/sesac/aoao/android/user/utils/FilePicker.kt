package kr.sesac.aoao.android.user.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity


/**
 * 갤러리에 사진 선택
 *
 * @since 2024.01.28
 * @author 이상민
 */
object FilePicker {

    private const val PICK_FILE_REQUEST = 1

    fun pickFile(activity: AppCompatActivity) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity.startActivityForResult(intent, PICK_FILE_REQUEST)
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?, onSuccess: (Uri) -> Unit, onError: () -> Unit) {
        if (requestCode == PICK_FILE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedFileUri: Uri? = data.data
            if (selectedFileUri != null) {
                onSuccess(selectedFileUri)
            } else {
                onError()
            }
        }
    }
}