package kr.sesac.aoao.android.common

import android.content.Context
import android.content.SharedPreferences

object TokenManager {

    private const val PREFS_NAME = "MyAppPrefs"
    private const val ACCESS_TOKEN_KEY = "access_token"
    private const val REFRESH_TOKEN_KEY = "refresh_token"
    private const val BEARER_TYPE = "Bearer"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // 저장된 Access Token을 가져옴
    fun getAccessToken(context: Context): String? {
        return getSharedPreferences(context).getString(ACCESS_TOKEN_KEY, null)
    }

    fun getAccessTokenWithTokenType(context: Context): String {
        return "$BEARER_TYPE ${getAccessToken(context)}"
    }

    // Access Token을 저장
    fun setAccessToken(context: Context, accessToken: String) {
        getSharedPreferences(context).edit().putString(ACCESS_TOKEN_KEY, accessToken).apply()
    }

    // 저장된 Refresh Token을 가져옴
    fun getRefreshToken(context: Context): String? {
        return getSharedPreferences(context).getString(REFRESH_TOKEN_KEY, null)
    }

    // Refresh Token을 저장
    fun setRefreshToken(context: Context, refreshToken: String) {
        getSharedPreferences(context).edit().putString(REFRESH_TOKEN_KEY, refreshToken).apply()
    }

    // 토큰 삭제
    fun clearTokens(context: Context) {
        getSharedPreferences(context).edit().remove(ACCESS_TOKEN_KEY).remove(REFRESH_TOKEN_KEY).apply()
    }
}
