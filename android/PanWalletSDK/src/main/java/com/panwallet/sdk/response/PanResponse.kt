package com.panwallet.sdk.response

import com.panwallet.sdk.config.ConnectType

data class PanResponse(

    var code: Int,

    var connectType: ConnectType,

    var message: String,
    
    var data: Map<String, String>
)
