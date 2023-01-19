package com.panwallet.sdk.error

internal data class ErrorModel(var code: Int, var description: String) {
    fun getName(): String {
        var name = ""
        when (code) {
            ErrorType.BALANCE.type -> {
                name = "Get balance error"
            }

            ErrorType.NOT_DOWNLOADED.type -> {
                name = "Pan wallet not downloaded"
            }

            ErrorType.CONFIG_FAILED.type -> {
                name = "Please use `setConfig` before"
            }

            else -> {
                name = "unknown"
            }
        }

        return name
    }
}