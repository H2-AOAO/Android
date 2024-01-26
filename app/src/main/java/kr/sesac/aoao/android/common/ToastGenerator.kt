package kr.sesac.aoao.android.common

import android.app.Activity
import android.widget.Toast

object ToastGenerator {

    fun showShortToast(message : String?, context: Activity) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
