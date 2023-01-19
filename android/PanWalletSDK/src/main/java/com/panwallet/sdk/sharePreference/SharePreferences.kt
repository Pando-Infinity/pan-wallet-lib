package com.panwallet.sdk.sharePreference

import android.content.Context
import com.panwallet.sdk.constant.Constants

internal class SharePreferences(context: Context) {

    companion object {
        var preferenceName = ""
    }


    private val sharePreferences =
        context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)

    fun saveAccessToken(keyAccess: String, accessToken: String?) {
        val editor = sharePreferences.edit()
        editor.putString(keyAccess, accessToken)
        editor.apply()
    }

    fun getAccessToken(keyAccess: String): String? = sharePreferences.getString(keyAccess, "")
}