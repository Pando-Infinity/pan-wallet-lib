package com.panwallet.sdk.config

enum class ConnectType(var type: String) {
    CONNECT("connect"),
    TRANSFER("transfer"),
    APPROVE("approve"),
    DEPOSIT_TOKEN("deposit_token"),
    WITHDRAW_TOKEN("withdraw_token"),
    BUY_NFT("buy_nft"),
    SELL_NFT("sell_nft"),
    SEND_NFT("send_nft"),
    STAKE_NFT("stake_nft"),
    UN_STAKE_NFT("unstake_nft"),
    BUY_BOX("buy_box"),
    UNLOCK_BOX("unlock_box"),
    OPEN_BOX("open_box"),
    SEND_BOX("send_box"),
    DISMISS("cancel_transaction"),
    CANCEL_TRANSACTION("cancel_transaction_non_success")
}