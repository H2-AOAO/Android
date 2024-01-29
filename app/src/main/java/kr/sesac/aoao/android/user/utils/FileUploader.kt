package kr.sesac.aoao.android.user.utils

import android.content.ContentResolver
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import kr.sesac.aoao.android.common.RetrofitConnection
import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.user.model.request.UserProfileUpdateResponse
import kr.sesac.aoao.android.user.service.UserService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

/**
 * 이미지 업로드 object
 *
 * @since 2024.01.28
 * @author 이상민
 */
object FileUploader {
    fun uploadFile(
        activity: AppCompatActivity,
        accessToken: String,
        fileUri: Uri,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val file = getFileFromUri(activity, fileUri)
            val requestFile = RequestBody.create(getMimeType(activity, fileUri)?.toMediaTypeOrNull(), file)
            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

            // API 호출
            val retrofit = RetrofitConnection.getInstance().create(UserService::class.java)
            val call: Call<ApplicationResponse<UserProfileUpdateResponse>> = retrofit.updateProfile(accessToken, body)
            call.enqueue(object : Callback<ApplicationResponse<UserProfileUpdateResponse>> {
                override fun onResponse(
                    call: Call<ApplicationResponse<UserProfileUpdateResponse>>,
                    response: Response<ApplicationResponse<UserProfileUpdateResponse>>
                ) {
                    if (response.isSuccessful) {
                        onSuccess()
                    } else {
                        onError("파일 업로드 실패: ${response.errorBody()}")
                    }
                }

                override fun onFailure(
                    call: Call<ApplicationResponse<UserProfileUpdateResponse>>,
                    t: Throwable
                ) {
                    onError("파일 업로드 실패: ${t.message}")
                }
            })
        } catch (e: Exception) {
            onError("파일 업로드 실패: ${e.message}")
        }
    }

    /**
     * Uri에서 파일을 가져와 캐시에 저장.
     *
     * @since 2024.01.29
     * @author 이상민
     */
    private fun getFileFromUri(activity: AppCompatActivity, uri: Uri): File {
        val contentResolver: ContentResolver = activity.contentResolver
        val mimeType = getMimeType(activity, uri)
        val fileExtension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
        val fileName = "uploaded_file_${System.currentTimeMillis()}.$fileExtension"
        return File(activity.cacheDir, fileName).apply {
            contentResolver.openInputStream(uri)?.use { input ->
                outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }
    }

    /**
     * 주어진 Uri로부터 MIME 유형을 추출하여 반환하는 함수
     *
     * @since 2024.01.29
     * @author 이상민
     */
    private fun getMimeType(activity: AppCompatActivity, uri: Uri): String? {
        return activity.contentResolver.getType(uri)
    }
}