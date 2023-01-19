package com.panwallet.sdk.error

internal object PanError {
    val notDownload = ErrorModel(ErrorType.NOT_DOWNLOADED.type, "Pan wallet has not been installed")

    val configNull = ErrorModel(ErrorType.CONFIG_FAILED.type, "Please use `setConfig` before")

    val notFromPan = ErrorModel(ErrorType.NOT_FROM_PAN.type, "Response is not from Pan wallet")

    val schemaError = ErrorModel(ErrorType.SCHEMA_ERROR.type, "dappScheme must be url")

    val logoUrlError = ErrorModel(ErrorType.LOGO_DAPP_ERROR.type, "logoDapp must be url")

    val debugConfigNull =
        ErrorModel(ErrorType.DEBUG_CONFIG_NULL.type, "debug config not nil if mode is debug")

    val dappNameEmpty = ErrorModel(ErrorType.DAPP_NAME_EMPTY.type, "name of dapp is empty")

    val hostError = ErrorModel(ErrorType.HOST_ERROR.type, "Host is the same as package name")

    val amountLesser0 = ErrorModel(
        ErrorType.AMOUNT_LESSER_0.type,
        "Amount must be a positive number and greater than 0"
    )

    val notConnectWallet = ErrorModel(ErrorType.NOT_CONNECT_WALLET.type, "Not connect wallet")

    val chainConnectAsNetwork = ErrorModel(
        ErrorType.CHAIN_CONNECT_AS_NETWORK.type,
        "Chain connect same as network transfer"
    )

    val currentWalletTypeNull = ErrorModel(ErrorType.CURRENT_WALLET_TYPE_NULL.type,"Current wallet connect type is null")
}