package com.panwallet.sdk.connection

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.panwallet.sdk.config.*
import com.panwallet.sdk.constant.Constants
import com.panwallet.sdk.error.PanError
import com.panwallet.sdk.response.PanResponse
import com.panwallet.sdk.sharePreference.SharePreferences
import com.panwallet.sdk.util.*
import com.panwallet.sdk.util.checkInstallPanWallet
import com.panwallet.sdk.util.getConnectRequest
import com.panwallet.sdk.util.isValidUri
import com.panwallet.sdk.util.isValidUrl

class PanWalletManager private constructor() {
    private var config: ConfigWalletService? = null
    private var currentWalletType: BlockChain? = null

    init {}

    private object Holder {
        val INSTANCE = PanWalletManager()
    }

    companion object {
        @JvmStatic
        fun getInstance(): PanWalletManager {
            return Holder.INSTANCE
        }
    }

    /*Setup Connect*/

    /**
     * Set up block chain config for `PanConnection`
     * @param config : Config blockchain and endpoint for your DAPP
     * @param context: context of activity android
     * @exception Throws exception when dappName empty, dapp uri not valid, host is the same as package name, debug Config is null
     */
    fun setConfig(config: ConfigWalletService, context: Context) {
        if (config.dappScheme.isEmpty()) {
            throw Exception(PanError.dappNameEmpty.description)
        } else if (!isValidUri(config.dappScheme)) {
            throw Exception(PanError.schemaError.description)
        } else if (config.logoDapp != null && isValidUrl(config.logoDapp!!)) {
            throw Exception(PanError.logoUrlError.description)
        } else {
            this.config = config
            SharePreferences.preferenceName = config.applicationID
        }
    }

    fun connectToWallet(chain: BlockChain, context: Context) {
        if (config != null) {
            config?.let { configWallet ->
                if (checkInstallPanWallet(context)) {
                    val intent = configWallet.getConnectRequest(chain, context)
                    context.startActivity(intent)
                } else {
                    throw Exception(PanError.notDownload.description)
                }
            }
        } else {
            throw Exception(PanError.configNull.description)
        }
    }

    fun setCurrentWalletTypeConnected(context: Context, currentWalletType: BlockChain) {
        if (checkConnectPanWallet(context, currentWalletType)) {
            this.currentWalletType = currentWalletType
        }
    }

    /**
     * send request disconnect
     * @param context: context of android
     * @param chain: wallet blockchain type is connected
     **/
    fun disconnect(context: Context, chain: BlockChain) {
        if (checkConnectWallet(context, chain)) {
            disconnectWallet(context, chain)
        } else {
            throw Exception(PanError.notConnectWallet.description)
        }
    }

    fun requestTransferToken(
        context: Context,
        network: BlockChain,
        contractAddress: String?,
        amount: Float,
        addressReceive: String,
    ) {
        if (checkNullCurrentWalletType() && checkConnectPanWallet(
                context,
                currentWalletType!!
            ) && checkChainConnectAndNetwork(currentWalletType!!, network)
        ) {
            if (amount >= 0) {
                val intent =
                    config!!.requestTransferToken(
                        context,
                        currentWalletType!!,
                        network,
                        contractAddress,
                        amount,
                        addressReceive,
                    )

                context.startActivity(intent)
            } else {
                throw Exception(PanError.amountLesser0.description)
            }
        }
    }

    fun requestApproveDepositToken(
        context: Context,
        network: BlockChain,
        contractAddress: String,
        addressSpender: String,
        amount: Float,
        transactionData: Map<String, Any>,
    ) {
        if (checkNullCurrentWalletType() && checkConnectPanWallet(
                context,
                currentWalletType!!
            ) && checkChainConnectAndNetwork(currentWalletType!!, network)
        ) {
            val intent =
                config!!.requestApprovedDepositToken(
                    context,
                    currentWalletType!!,
                    network,
                    contractAddress,
                    addressSpender,
                    amount,
                    transactionData
                )

            context.startActivity(intent)
        }
    }

    fun requestDepositToken(
        context: Context,
        network: BlockChain,
        contractAddress: String,
        amount: Float,
        addressSpender: String,
        transactionData: Map<String, Any>
    ) {
        if (checkNullCurrentWalletType() && checkConnectPanWallet(
                context,
                currentWalletType!!
            ) && checkChainConnectAndNetwork(currentWalletType!!, network)
        ) {
            if (amount > 0) {
                val intent =
                    config!!.requestDepositToken(
                        context,
                        currentWalletType!!,
                        network,
                        contractAddress,
                        amount,
                        addressSpender,
                        transactionData
                    )
                context.startActivity(intent)
            } else {
                throw Exception(PanError.amountLesser0.description)
            }
        }
    }

    fun requestApproveWithdrawToken(
        context: Context,
        network: BlockChain,
        contractAddress: String,
        addressSpender: String,
        amount: String,
        transactionData: Map<String, Any>,
    ) {
        if (checkNullCurrentWalletType() && checkConnectPanWallet(
                context,
                currentWalletType!!
            ) && checkChainConnectAndNetwork(currentWalletType!!, network)
        ) {
            val intent =
                config!!.requestApprovedWithdrawToken(
                    context,
                    currentWalletType!!,
                    network,
                    contractAddress,
                    addressSpender,
                    amount,
                    transactionData
                )

            context.startActivity(intent)
        }
    }

    fun requestWithdrawToken(
        context: Context,
        network: BlockChain,
        contractAddress: String,
        amount: Float,
        transactionData: Map<String, Any>,
    ) {
        if (checkNullCurrentWalletType() && checkConnectPanWallet(
                context,
                currentWalletType!!
            ) && checkChainConnectAndNetwork(currentWalletType!!, network)
        ) {
            if (amount > 0) {
                val intent =
                    config!!.requestWithdrawToken(
                        context,
                        currentWalletType!!,
                        network,
                        contractAddress,
                        amount,
                        transactionData
                    )

                context.startActivity(intent)
            } else {
                throw Exception(PanError.amountLesser0.description)
            }
        }
    }

    fun requestApproveBuyNft(
        context: Context,
        network: BlockChain,
        contractAddress: String,
        addressSpender: String,
        tokenSymbol: String,
        amount: String,
        transactionData: Map<String, Any>,
    ) {
        if (checkNullCurrentWalletType() && checkConnectPanWallet(
                context,
                currentWalletType!!
            ) && checkChainConnectAndNetwork(currentWalletType!!, network)
        ) {
            if (amount.toFloat() > 0) {
                val intent = config!!.requestApproveNft(
                    context,
                    ConnectType.BUY_NFT,
                    currentWalletType!!,
                    network,
                    contractAddress,
                    addressSpender,
                    null,
                    tokenSymbol,
                    amount,
                    transactionData
                )
                context.startActivity(intent)
            } else {
                throw Exception(PanError.amountLesser0.description)
            }
        }
    }

    fun requestBuyNFT(
        context: Context,
        network: BlockChain,
        contractAddress: String,
        tokenSymbol: String,
        nft: NFT,
        addressSpender: String,
        transactionData: Map<String, Any>,
    ) {
        if (checkNullCurrentWalletType() && checkConnectPanWallet(
                context,
                currentWalletType!!
            ) && checkChainConnectAndNetwork(currentWalletType!!, network)
        ) {
            val intent =
                config!!.requestTransferNft(
                    context,
                    ConnectType.BUY_NFT,
                    currentWalletType!!,
                    network,
                    contractAddress,
                    tokenSymbol,
                    nft,
                    addressSpender,
                    null,
                    null,
                    transactionData
                )

            context.startActivity(intent)
        }
    }

    fun requestApproveSellNft(
        context: Context,
        network: BlockChain,
        contractAddress: String,
        addressOperator: String,
        transactionData: Map<String, Any>,
    ) {
        if (checkNullCurrentWalletType() && checkConnectPanWallet(
                context,
                currentWalletType!!
            ) && checkChainConnectAndNetwork(currentWalletType!!, network)
        ) {
            val intent =
                config!!.requestApproveNft(
                    context,
                    ConnectType.SELL_NFT,
                    currentWalletType!!,
                    network,
                    contractAddress,
                    null,
                    addressOperator,
                    null,
                    null,
                    transactionData
                )
            context.startActivity(intent)
        }
    }

    /**
     * send request sell NFT
     * @param context: context of android
     * @param network: network transfer token
     * @param contractAddress: contract address of NFT
     * @param tokenSymbol: token unit of dApp
     * @param nft: object NFT(id, image, name, price)
     * @param addressOperator: address is approved
     * @param transactionData: data is generated of function transfer nft
     **/
    fun requestSellNFT(
        context: Context,
        network: BlockChain,
        contractAddress: String,
        tokenSymbol: String,
        nft: NFT,
        addressOperator: String,
        transactionData: Map<String, Any>,
    ) {
        if (checkNullCurrentWalletType() && checkConnectPanWallet(
                context,
                currentWalletType!!
            ) && checkChainConnectAndNetwork(currentWalletType!!, network)
        ) {
            val intent =
                config!!.requestTransferNft(
                    context,
                    ConnectType.SELL_NFT,
                    currentWalletType!!,
                    network,
                    contractAddress,
                    tokenSymbol,
                    nft,
                    null,
                    addressOperator,
                    null,
                    transactionData
                )

            context.startActivity(intent)
        }
    }

    fun requestSendNFT(
        context: Context,
        network: BlockChain,
        contractAddress: String,
        nft: NFT,
        toAddress: String,
        transactionData: Map<String, Any>,
    ) {
        if (checkNullCurrentWalletType() && checkConnectPanWallet(
                context,
                currentWalletType!!
            ) && checkChainConnectAndNetwork(currentWalletType!!, network)
        ) {
            val intent =
                config!!.requestTransferNft(
                    context,
                    ConnectType.SEND_NFT,
                    currentWalletType!!,
                    network,
                    contractAddress,
                    null,
                    nft,
                    null,
                    null,
                    toAddress,
                    transactionData
                )

            context.startActivity(intent)
        }
    }

    /**
     *  send request approve stake nft
     *  @param context: context of android
     *  @param network: network transfer token
     *  @param contractAddress: contract address of NFT
     *  @param transactionData: data is generated of function transfer nft
     *  */
    fun requestApproveStakeNft(
        context: Context,
        network: BlockChain,
        contractAddress: String,
        addressOperator: String,
        transactionData: Map<String, Any>
    ) {
        if (checkNullCurrentWalletType() && checkConnectPanWallet(
                context,
                currentWalletType!!
            ) && checkChainConnectAndNetwork(currentWalletType!!, network)
        ) {
            val intent =
                config!!.requestApproveNft(
                    context,
                    ConnectType.STAKE_NFT,
                    currentWalletType!!,
                    network,
                    contractAddress,
                    null,
                    addressOperator,
                    null,
                    null,
                    transactionData
                )

            context.startActivity(intent)
        }
    }

    fun requestStakeNft(
        context: Context,
        network: BlockChain,
        contractAddress: String,
        nft: NFT,
        addressOperator: String,
        transactionData: Map<String, Any>,
    ) {
        if (checkNullCurrentWalletType() && checkConnectPanWallet(
                context,
                currentWalletType!!
            ) && checkChainConnectAndNetwork(currentWalletType!!, network)
        ) {
            val intent =
                config!!.requestTransferNft(
                    context,
                    ConnectType.STAKE_NFT,
                    currentWalletType!!,
                    network,
                    contractAddress,
                    null,
                    nft,
                    null,
                    addressOperator,
                    null,
                    transactionData
                )

            context.startActivity(intent)
        }
    }

    /**
     *  send request approve unstake nft
     *  @param context: context of android
     *  @param network: network transfer token
     *  @param contractAddress: contract address of NFT
     *  @param transactionData: data is generated of function transfer nft
     *  */
    fun requestApproveUnStakeNft(
        context: Context,
        network: BlockChain,
        contractAddress: String,
        addressSpender: String,
        amount: String,
        tokenSymbol: String,
        transactionData: Map<String, Any>
    ) {
        if (checkNullCurrentWalletType() && checkConnectPanWallet(
                context,
                currentWalletType!!
            ) && checkChainConnectAndNetwork(currentWalletType!!, network)
        ) {
            val intent =
                config!!.requestApproveNft(
                    context,
                    ConnectType.UN_STAKE_NFT,
                    currentWalletType!!,
                    network,
                    contractAddress,
                    addressSpender,
                    null,
                    tokenSymbol,
                    amount,
                    transactionData
                )

            context.startActivity(intent)
        }
    }

    fun requestUnStakeNft(
        context: Context,
        network: BlockChain,
        contractAddress: String,
        nft: NFT,
        amount: String,
        addressSpender: String,
        transactionData: Map<String, Any>,
    ) {
        if (checkNullCurrentWalletType() && checkConnectPanWallet(
                context,
                currentWalletType!!
            ) && checkChainConnectAndNetwork(currentWalletType!!, network)
        ) {
            val intent =
                config!!.requestTransferNft(
                    context,
                    ConnectType.UN_STAKE_NFT,
                    currentWalletType!!,
                    network,
                    contractAddress,
                    null,
                    nft,
                    addressSpender,
                    null,
                    null,
                    transactionData,
                    amount
                )

            context.startActivity(intent)
        }
    }

    fun requestApproveBuyBox(
        context: Context,
        network: BlockChain,
        contractAddress: String,
        addressOperator: String,
        transactionData: Map<String, Any>
    ) {
        if (checkNullCurrentWalletType() && checkConnectPanWallet(
                context,
                currentWalletType!!
            ) && checkChainConnectAndNetwork(currentWalletType!!, network)
        ) {
            val intent =
                config!!.requestApproveBox(
                    context,
                    ConnectType.BUY_BOX,
                    currentWalletType!!,
                    network,
                    contractAddress,
                    addressOperator,
                    transactionData
                )

            context.startActivity(intent)
        }
    }

    /**
     * send request buy box
     * @param context: context of android
     * @param network: network transfer token
     * @param contractAddress: contract address
     * @param addressOperator: address is approved
     * @param transactionData: data is generated of function unlock box
     **/
    fun requestBuyBox(
        context: Context,
        network: BlockChain,
        nameBox: String,
        contractAddress: String,
        addressOperator: String,
        transactionData: Map<String, Any>
    ) {
        if (checkNullCurrentWalletType() && checkConnectPanWallet(
                context,
                currentWalletType!!
            ) && checkChainConnectAndNetwork(currentWalletType!!, network)
        ) {
            val intent =
                config!!.requestTransferBox(
                    context,
                    ConnectType.BUY_BOX,
                    currentWalletType!!,
                    network,
                    nameBox,
                    contractAddress,
                    addressOperator,
                    null,
                    transactionData
                )

            context.startActivity(intent)
        }
    }

    fun requestApproveUnlockBox(
        context: Context,
        network: BlockChain,
        contractAddress: String,
        addressOperator: String,
        transactionData: Map<String, Any>
    ) {
        if (checkNullCurrentWalletType() && checkConnectPanWallet(
                context,
                currentWalletType!!
            ) && checkChainConnectAndNetwork(currentWalletType!!, network)
        ) {
            val intent =
                config!!.requestApproveBox(
                    context,
                    ConnectType.UNLOCK_BOX,
                    currentWalletType!!,
                    network,
                    contractAddress,
                    addressOperator,
                    transactionData
                )

            context.startActivity(intent)
        }
    }

    fun requestUnlockBox(
        context: Context,
        network: BlockChain,
        nameBox: String,
        contractAddress: String,
        addressOperator: String,
        transactionData: Map<String, Any>
    ) {
        if (checkNullCurrentWalletType() && checkConnectPanWallet(
                context,
                currentWalletType!!
            ) && checkChainConnectAndNetwork(
                currentWalletType!!,
                network
            )
        ) {
            val intent =
                config!!.requestTransferBox(
                    context,
                    ConnectType.UNLOCK_BOX,
                    currentWalletType!!,
                    network,
                    nameBox,
                    contractAddress,
                    addressOperator,
                    null,
                    transactionData
                )

            context.startActivity(intent)
        }
    }

    fun requestOpenBox(
        context: Context,
        network: BlockChain,
        nameBox: String,
        contractAddress: String,
        transactionData: Map<String, Any>
    ) {
        if (checkNullCurrentWalletType() && checkConnectPanWallet(
                context,
                currentWalletType!!
            ) && checkChainConnectAndNetwork(currentWalletType!!, network)
        ) {
            val intent =
                config!!.requestTransferBox(
                    context,
                    ConnectType.OPEN_BOX,
                    currentWalletType!!,
                    network,
                    nameBox,
                    contractAddress,
                    null,
                    null,
                    transactionData
                )

            context.startActivity(intent)
        }
    }

    fun requestSendBox(
        context: Context,
        network: BlockChain,
        nameBox: String,
        contractAddress: String,
        toAddress: String,
        transactionData: Map<String, Any>
    ) {
        if (checkNullCurrentWalletType() && checkConnectPanWallet(
                context,
                currentWalletType!!
            ) && checkChainConnectAndNetwork(currentWalletType!!, network)
        ) {
            val intent =
                config!!.requestTransferBox(
                    context,
                    ConnectType.SEND_BOX,
                    currentWalletType!!,
                    network,
                    nameBox,
                    contractAddress,
                    null,
                    toAddress,
                    transactionData
                )

            context.startActivity(intent)
        }
    }

    fun requestCancelTransaction(
        context: Context,
        chain: BlockChain? = null,
        contractAddress: String? = null,
        transactionData: Map<String, Any> = emptyMap()
    ) {
        if (checkNullCurrentWalletType() && checkConnectPanWallet(
                context,
                currentWalletType!!
            )
        ) {
            val intent =
                config!!.requestCancelTransaction(
                    context,
                    currentWalletType!!,
                    chain,
                    contractAddress,
                    transactionData
                )

            context.startActivity(intent)
        }
    }

    fun getActionNameBroadcast(): String = Constants.actionName

    fun getDataResponse(
        context: Context,
        intent: Intent,
    ): PanResponse {
        val url = intent.getStringExtra("url")
        val uri = Uri.parse(url)
        return convertDataToPanResponse(context, uri)
    }

    fun getDataResponse(context: Context, url: String): PanResponse {
        val uri = Uri.parse(url)
        return convertDataToPanResponse(context, uri)
    }

    private fun convertDataToPanResponse(context: Context, uri: Uri): PanResponse {
        val pathSegments = uri.pathSegments
        if (uri.host != null && pathSegments[0] == "pan") {
            val code = uri.getQueryParameter("code")?.toInt() ?: 0
            val message = uri.getQueryParameter("message") ?: ""
            val accessToken = uri.getQueryParameter("pan_access_token")
            accessToken?.let {
                val keyAccess = it.split("-")[0]

                SharePreferences(context).saveAccessToken(keyAccess, accessToken)
            }
            val responseType: ConnectType = when (pathSegments[1]) {
                ConnectType.CONNECT.type -> ConnectType.CONNECT

                ConnectType.TRANSFER.type -> ConnectType.TRANSFER

                ConnectType.APPROVE.type -> ConnectType.APPROVE

                ConnectType.DEPOSIT_TOKEN.type -> ConnectType.DEPOSIT_TOKEN

                ConnectType.WITHDRAW_TOKEN.type -> ConnectType.WITHDRAW_TOKEN

                ConnectType.BUY_NFT.type -> ConnectType.BUY_NFT

                ConnectType.SELL_NFT.type -> ConnectType.SELL_NFT

                ConnectType.SEND_NFT.type -> ConnectType.SEND_NFT

                ConnectType.STAKE_NFT.type -> ConnectType.STAKE_NFT

                ConnectType.UN_STAKE_NFT.type -> ConnectType.UN_STAKE_NFT

                ConnectType.BUY_BOX.type -> ConnectType.BUY_BOX

                ConnectType.UNLOCK_BOX.type -> ConnectType.UNLOCK_BOX

                ConnectType.OPEN_BOX.type -> ConnectType.OPEN_BOX

                ConnectType.SEND_BOX.type -> ConnectType.SEND_BOX

                ConnectType.CANCEL_TRANSACTION.type -> ConnectType.CANCEL_TRANSACTION

                ConnectType.DISMISS.type -> ConnectType.DISMISS

                else -> ConnectType.CONNECT
            }

            val dataResponse = mutableMapOf<String, String>()
            uri.query?.split("&")?.forEach { item ->
                val queryParam = item.split("=")
                val key = queryParam[0]
                val value = queryParam[1]
                if (key != "code" && key != "message" && key != "pan_access_token") {
                    if (isValidJson(value)) {
                        value.replace("{", "")
                            .replace("}", "")
                            .split(",")
                            .forEach { i ->
                                val keyValue = i.split(":")
                                dataResponse[keyValue[0].replace("\"", "")] =
                                    keyValue[1].replace("\"", "")
                            }
                    } else {
                        dataResponse[key] = value
                    }

                }
            }

            dataResponse["chain"]?.let {
                setCurrentWalletType(responseType, code, it)
            }


            return PanResponse(code, responseType, message, dataResponse)
        } else {
            throw Exception(PanError.notFromPan.description)
        }
    }

    private fun checkConnectPanWallet(context: Context, chain: BlockChain): Boolean {
        if (config == null) {
            throw Exception(PanError.configNull.description)
        } else {
            if (!checkInstallPanWallet(context)) {
                throw Exception(PanError.notDownload.description)
            } else {
                if (!checkConnectWallet(context, chain)) {
                    throw Exception(PanError.notConnectWallet.description)
                } else {
                    return true
                }
            }
        }
    }

    private fun checkChainConnectAndNetwork(
        chainConnect: BlockChain,
        network: BlockChain
    ): Boolean {
        return if (chainConnect !== BlockChain.MULTI_CHAIN) {
            if (chainConnect === network) {
                true
            } else {
                throw Exception(PanError.chainConnectAsNetwork.description)
            }
        } else {
            true
        }
    }

    private fun checkNullCurrentWalletType(): Boolean =
        if (currentWalletType !== null) true else throw Exception(PanError.currentWalletTypeNull.description)

    private fun setCurrentWalletType(responseType: ConnectType, code: Int, chain: String) {
        if (responseType === ConnectType.CONNECT && code == 200) {
            this.currentWalletType = when (chain) {
                BlockChain.MULTI_CHAIN.symbol -> BlockChain.MULTI_CHAIN
                BlockChain.BITCOIN.symbol -> BlockChain.BITCOIN
                BlockChain.ETHEREUM.symbol -> BlockChain.ETHEREUM
                BlockChain.BINANCE_SMART_CHAIN.symbol -> BlockChain.BINANCE_SMART_CHAIN
                else -> {
                    BlockChain.SOLANA
                }
            }
        }
    }
}
