package com.panwallet.sdk.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import com.google.gson.Gson
import com.panwallet.sdk.config.BlockChain
import com.panwallet.sdk.config.ConfigWalletService
import com.panwallet.sdk.config.ConnectType
import com.panwallet.sdk.config.NFT
import com.panwallet.sdk.constant.ParamKeys
import com.panwallet.sdk.sharePreference.SharePreferences

internal fun ConfigWalletService.getConnectRequest(chain: BlockChain, context: Context): Intent {
    val accessToken = SharePreferences(context).getAccessToken(chain.symbol)

    val builder = Uri.Builder().apply {
        scheme(ParamKeys.scheme)
        authority(ParamKeys.authority)
        appendPath(ConnectType.CONNECT.type)
        appendQueryParameter(ParamKeys.name, dappName)
        appendQueryParameter(ParamKeys.schemeDapp, dappScheme)
        appendQueryParameter(ParamKeys.url, dappWebUrl)
        appendQueryParameter(ParamKeys.logo, logoDapp ?: "")
        appendQueryParameter(ParamKeys.chain, chain.symbol)
        accessToken?.let {
            appendQueryParameter(ParamKeys.panAccessToken, it)
        }
        appendQueryParameter(ParamKeys.bundle, applicationID)
    }

    val uri = Uri.parse(builder.toString())

    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

    return intent
}

internal fun ConfigWalletService.requestTransferToken(
    context: Context,
    chainConnect: BlockChain,
    network: BlockChain,
    contractAddress: String?,
    amount: Float,
    addressReceive: String,
): Intent {
    val accessToken = SharePreferences(context).getAccessToken(chainConnect.symbol)

    val builder = Uri.Builder().apply {
        scheme(ParamKeys.scheme)
        authority(ParamKeys.authority)
        appendPath(ConnectType.TRANSFER.type)
        appendQueryParameter(ParamKeys.schemeDapp, dappScheme)
        appendQueryParameter(ParamKeys.panAccessToken, accessToken)
        appendQueryParameter(ParamKeys.chain, network.symbol)
        appendQueryParameter(ParamKeys.contractAddress, contractAddress)
        appendQueryParameter(ParamKeys.amount, amount.toString())
        appendQueryParameter(ParamKeys.addressReceive, addressReceive)
        appendQueryParameter(ParamKeys.bundle, applicationID)
    }

    val uri = Uri.parse(builder.toString())

    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

    return intent
}

internal fun ConfigWalletService.requestApprovedDepositToken(
    context: Context,
    chainConnect: BlockChain,
    network: BlockChain,
    contractAddress: String,
    addressSpender: String,
    amount: Float,
    transactionData: Map<String, Any>,
): Intent {
    val accessToken = SharePreferences(context).getAccessToken(chainConnect.symbol)

    val builder = Uri.Builder().apply {
        scheme(ParamKeys.scheme)
        authority(ParamKeys.authority)
        appendPath(ConnectType.APPROVE.type)
        appendQueryParameter(ParamKeys.schemeDapp, dappScheme)
        appendQueryParameter(ParamKeys.panAccessToken, accessToken)
        appendQueryParameter(ParamKeys.type, ConnectType.DEPOSIT_TOKEN.type)
        appendQueryParameter(ParamKeys.chain, network.symbol)
        appendQueryParameter(ParamKeys.contractAddress, contractAddress)
        appendQueryParameter(ParamKeys.addressSpender, addressSpender)
        appendQueryParameter(ParamKeys.amount, amount.toString())
        appendQueryParameter(ParamKeys.transactionData, Gson().toJson(transactionData))
        appendQueryParameter(ParamKeys.bundle, applicationID)

    }

    val uri = Uri.parse(builder.toString())

    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

    return intent
}

internal fun ConfigWalletService.requestApprovedWithdrawToken(
    context: Context,
    chainConnect: BlockChain,
    network: BlockChain,
    contractAddress: String,
    addressSpender: String,
    amount: String,
    transactionData: Map<String, Any>,
): Intent {
    val accessToken = SharePreferences(context).getAccessToken(chainConnect.symbol)

    val builder = Uri.Builder().apply {
        scheme(ParamKeys.scheme)
        authority(ParamKeys.authority)
        appendPath(ConnectType.APPROVE.type)
        appendQueryParameter(ParamKeys.schemeDapp, dappScheme)
        appendQueryParameter(ParamKeys.panAccessToken, accessToken)
        appendQueryParameter(ParamKeys.type, ConnectType.WITHDRAW_TOKEN.type)
        appendQueryParameter(ParamKeys.chain, network.symbol)
        appendQueryParameter(ParamKeys.contractAddress, contractAddress)
        appendQueryParameter(ParamKeys.addressSpender, addressSpender)
        appendQueryParameter(ParamKeys.amount, amount)
        appendQueryParameter(ParamKeys.transactionData, Gson().toJson(transactionData))
        appendQueryParameter(ParamKeys.bundle, applicationID)
    }

    val uri = Uri.parse(builder.toString())

    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

    return intent
}

internal fun ConfigWalletService.requestDepositToken(
    context: Context,
    chainConnect: BlockChain,
    network: BlockChain,
    contractAddress: String,
    amount: Float,
    addressSpender: String,
    transactionData: Map<String, Any>
): Intent {
    val accessToken = SharePreferences(context).getAccessToken(chainConnect.symbol)

    val builder = Uri.Builder().apply {
        scheme(ParamKeys.scheme)
        authority(ParamKeys.authority)
        appendPath(ConnectType.DEPOSIT_TOKEN.type)
        appendQueryParameter(ParamKeys.schemeDapp, dappScheme)
        appendQueryParameter(ParamKeys.panAccessToken, accessToken)
        appendQueryParameter(ParamKeys.chain, network.symbol)
        appendQueryParameter(ParamKeys.contractAddress, contractAddress)
        appendQueryParameter(ParamKeys.amount, amount.toString())
        appendQueryParameter(ParamKeys.addressSpender, addressSpender)
        appendQueryParameter(ParamKeys.transactionData, Gson().toJson(transactionData))
        appendQueryParameter(ParamKeys.bundle, applicationID)
    }

    val uri = Uri.parse(builder.toString())

    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

    return intent
}

internal fun ConfigWalletService.requestWithdrawToken(
    context: Context,
    chainConnect: BlockChain,
    network: BlockChain,
    contractAddress: String,
    amount: Float,
    transactionData: Map<String, Any>,
): Intent {
    val accessToken = SharePreferences(context).getAccessToken(chainConnect.symbol)

    val builder = Uri.Builder().apply {
        scheme(ParamKeys.scheme)
        authority(ParamKeys.authority)
        appendPath(ConnectType.WITHDRAW_TOKEN.type)
        appendQueryParameter(ParamKeys.schemeDapp, dappScheme)
        appendQueryParameter(ParamKeys.panAccessToken, accessToken)
        appendQueryParameter(ParamKeys.chain, network.symbol)
        appendQueryParameter(ParamKeys.contractAddress, contractAddress)
        appendQueryParameter(ParamKeys.amount, amount.toString())
        appendQueryParameter(ParamKeys.transactionData, Gson().toJson(transactionData))
        appendQueryParameter(ParamKeys.bundle, applicationID)
    }

    val uri = Uri.parse(builder.toString())

    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

    return intent
}

internal fun ConfigWalletService.requestApproveNft(
    context: Context,
    connectType: ConnectType,
    chainConnect: BlockChain,
    network: BlockChain,
    contractAddress: String,
    addressSpender: String?,
    addressOperator: String?,
    tokenSymbol: String?,
    amount: String?,
    transactionData: Map<String, Any>
): Intent {
    val accessToken = SharePreferences(context).getAccessToken(chainConnect.symbol)
    val builder = Uri.Builder().apply {
        scheme(ParamKeys.scheme)
        authority(ParamKeys.authority)
        appendPath(ConnectType.APPROVE.type)
        appendQueryParameter(ParamKeys.schemeDapp, dappScheme)
        appendQueryParameter(ParamKeys.panAccessToken, accessToken)
        appendQueryParameter(ParamKeys.type, connectType.type)
        appendQueryParameter(ParamKeys.chain, network.symbol)
        appendQueryParameter(ParamKeys.contractAddress, contractAddress)
        addressSpender?.let { appendQueryParameter(ParamKeys.addressSpender, addressSpender) }
        addressOperator?.let { appendQueryParameter(ParamKeys.addressOperator, addressOperator) }
        tokenSymbol?.let { appendQueryParameter(ParamKeys.tokenSymbol, tokenSymbol) }
        amount?.let { appendQueryParameter(ParamKeys.amount, amount.toString()) }
        appendQueryParameter(ParamKeys.transactionData, Gson().toJson(transactionData))
        appendQueryParameter(ParamKeys.bundle, applicationID)

    }

    val uri = Uri.parse(builder.toString())

    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

    return intent
}

internal fun ConfigWalletService.requestTransferNft(
    context: Context,
    connectType: ConnectType,
    chainConnect: BlockChain,
    network: BlockChain,
    contractAddress: String,
    tokenSymbol: String?,
    nft: NFT,
    addressSpender: String?,
    addressOperator: String?,
    toAddress: String?,
    transactionData: Map<String, Any>,
    amount: String? = null,
): Intent {
    val accessToken = SharePreferences(context).getAccessToken(chainConnect.symbol)

    val builder = Uri.Builder().apply {
        scheme(ParamKeys.scheme)
        authority(ParamKeys.authority)
        appendPath(connectType.type)
        appendQueryParameter(ParamKeys.schemeDapp, dappScheme)
        appendQueryParameter(ParamKeys.panAccessToken, accessToken)
        appendQueryParameter(ParamKeys.chain, network.symbol)
        appendQueryParameter(ParamKeys.contractAddress, contractAddress)
        tokenSymbol?.let { appendQueryParameter(ParamKeys.tokenSymbol, tokenSymbol) }
        appendQueryParameter(ParamKeys.nft, Gson().toJson(nft))
        addressSpender?.let { appendQueryParameter(ParamKeys.addressSpender, addressSpender) }
        addressOperator?.let { appendQueryParameter(ParamKeys.addressOperator, addressOperator) }
        toAddress?.let { appendQueryParameter(ParamKeys.toAddress, toAddress) }
        amount?.let { appendQueryParameter(ParamKeys.amount, amount) }
        appendQueryParameter(ParamKeys.transactionData, Gson().toJson(transactionData))
        appendQueryParameter(ParamKeys.bundle, applicationID)
    }

    val uri = Uri.parse(builder.toString())

    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

    return intent
}


internal fun ConfigWalletService.requestApproveBox(
    context: Context,
    type: ConnectType,
    chainConnect: BlockChain,
    network: BlockChain,
    contractAddress: String,
    addressOperator: String,
    transactionData: Map<String, Any>
): Intent {
    val accessToken = SharePreferences(context).getAccessToken(chainConnect.symbol)

    val builder = Uri.Builder().apply {
        scheme(ParamKeys.scheme)
        authority(ParamKeys.authority)
        appendPath(ConnectType.APPROVE.type)
        appendQueryParameter(ParamKeys.schemeDapp, dappScheme)
        appendQueryParameter(ParamKeys.panAccessToken, accessToken)
        appendQueryParameter(ParamKeys.type, type.type)
        appendQueryParameter(ParamKeys.chain, network.symbol)
        appendQueryParameter(ParamKeys.contractAddress, contractAddress)
        appendQueryParameter(ParamKeys.addressOperator, addressOperator)
        appendQueryParameter(ParamKeys.transactionData, Gson().toJson(transactionData))
        appendQueryParameter(ParamKeys.bundle, applicationID)
    }

    val uri = Uri.parse(builder.toString())

    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

    return intent
}

internal fun ConfigWalletService.requestTransferBox(
    context: Context,
    type: ConnectType,
    chainConnect: BlockChain,
    network: BlockChain,
    nameBox: String,
    contractAddress: String,
    addressOperator: String?,
    toAddress: String?,
    transactionData: Map<String, Any>
): Intent {
    val accessToken = SharePreferences(context).getAccessToken(chainConnect.symbol)

    val builder = Uri.Builder().apply {
        scheme(ParamKeys.scheme)
        authority(ParamKeys.authority)
        appendPath(type.type)
        appendQueryParameter(ParamKeys.schemeDapp, dappScheme)
        appendQueryParameter(ParamKeys.panAccessToken, accessToken)
        appendQueryParameter(ParamKeys.chain, network.symbol)
        appendQueryParameter(ParamKeys.nameBox, nameBox)
        appendQueryParameter(ParamKeys.contractAddress, contractAddress)
        addressOperator?.let { appendQueryParameter(ParamKeys.addressOperator, addressOperator) }
        toAddress?.let { appendQueryParameter(ParamKeys.toAddress, toAddress) }
        appendQueryParameter(ParamKeys.transactionData, Gson().toJson(transactionData))
        appendQueryParameter(ParamKeys.bundle, applicationID)
    }

    val uri = Uri.parse(builder.toString())

    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

    return intent
}

internal fun ConfigWalletService.requestCancelTransaction(
    context: Context,
    chainConnect: BlockChain,
    chain: BlockChain?,
    contractAddress: String?,
    transactionData: Map<String, Any>
): Intent {
    val accessToken = SharePreferences(context).getAccessToken(chainConnect.symbol)

    val builder = Uri.Builder().apply {
        scheme(ParamKeys.scheme)
        authority(ParamKeys.authority)
        if (chain != null && contractAddress != null && transactionData.isNotEmpty()) {
            appendPath(ConnectType.CANCEL_TRANSACTION.type)
            appendQueryParameter(ParamKeys.transactionData, Gson().toJson(transactionData))
            appendQueryParameter(ParamKeys.chain, chain.symbol)
            appendQueryParameter(ParamKeys.contractAddress, contractAddress)
        } else {
            appendPath(ConnectType.DISMISS.type)
        }
        appendQueryParameter(ParamKeys.schemeDapp, dappScheme)
        appendQueryParameter(ParamKeys.panAccessToken, accessToken)
        appendQueryParameter(ParamKeys.bundle, applicationID)
    }

    val uri = Uri.parse(builder.toString())

    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

    return intent
}

internal fun checkInstallPanWallet(context: Context): Boolean {
    return try {
        val packageManager = context.packageManager
        val isInstalled = packageManager.getApplicationInfo("com.panwallet.app.dev", 0).enabled
        isInstalled
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

internal fun disconnectWallet(context: Context, chain: BlockChain) {
    SharePreferences(context).saveAccessToken(chain.symbol, "")
}

internal fun checkConnectWallet(context: Context, chain: BlockChain): Boolean =
    SharePreferences(context).getAccessToken(chain.symbol) !== ""