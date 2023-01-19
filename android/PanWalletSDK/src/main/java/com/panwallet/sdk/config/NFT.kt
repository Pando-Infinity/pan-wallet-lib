package com.panwallet.sdk.config

data class NFT(
    var id: String,
    var image: String?,
    var name: String,
    var price: Float?
) {
    companion object {
        fun create(id: String, image: String?, name: String, price: Float?): NFT {
            return NFT(
                id,
                image,
                name,
                price
            )
        }
    }
}
