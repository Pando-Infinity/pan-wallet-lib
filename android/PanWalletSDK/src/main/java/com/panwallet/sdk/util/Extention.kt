package com.panwallet.sdk.util

import android.content.Context
import android.net.Uri
import android.webkit.URLUtil
import org.json.JSONException
import org.json.JSONObject

internal fun isValidUri(url: String): Boolean {
    val uri = Uri.parse(url)
    val scheme = uri.scheme
    val host = uri.host

    return scheme != null && host != null
}

internal fun isValidUrl(url: String): Boolean = URLUtil.isValidUrl(url)

internal fun isValidJson(json:String):Boolean{
    try {
        JSONObject(json)
    } catch (e: JSONException) {
        return false
    }
    return true
}