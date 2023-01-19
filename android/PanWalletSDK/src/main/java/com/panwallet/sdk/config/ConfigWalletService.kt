package com.panwallet.sdk.config

data class ConfigWalletService(
    var dappName: String,
    var dappScheme: String,
    var dappWebUrl: String,
    var logoDapp: String?,
    var applicationID: String
) {
    companion object {

        @JvmStatic
        fun create(dappName: String, dappScheme: String, dappWebUrl: String, logoDapp: String?, applicationID: String): ConfigWalletService {
            return ConfigWalletService(
                dappName,
                dappScheme,
                dappWebUrl,
                logoDapp,
                applicationID
            )
        }
    }
}