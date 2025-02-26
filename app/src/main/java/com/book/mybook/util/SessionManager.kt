package com.book.mybook.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)

    companion object {
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"
    }

    fun saveTokens(accessToken: String, refreshToken: String) {
        val editor = sharedPreferences.edit()
        editor.putString(ACCESS_TOKEN, accessToken)
        editor.putString(REFRESH_TOKEN, refreshToken)
        editor.apply()
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString(ACCESS_TOKEN, null)
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString(REFRESH_TOKEN, null)
    }

    fun clearTokens() {
        val editor = sharedPreferences.edit()
        editor.remove(ACCESS_TOKEN)
        editor.remove(REFRESH_TOKEN)
        editor.apply()
    }

}