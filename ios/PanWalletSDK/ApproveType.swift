//
//  ApproveType.swift
//  PanWalletSDK
//
//  Created by jujien on 05/09/2022.
//

import Foundation

public enum ApproveType: String {
  case buyNFT = "buy_nft"
  case sellNFT = "sell_nft"
  case depositToken = "deposit_token"
  case withdrawToken = "with_address"
  case unlockBox = "unlock_box"
  case buyBox = "buy_box"
  case stakeNFT = "stake_nft"
  case unStakeNFT = "unstake_nft"
}

public enum ObjectAddress {
    case `operator`(String)
    case spender(String)
    case destination(String)
}
