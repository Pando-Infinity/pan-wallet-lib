//
//  ConnectType.swift
//  PanWalletSDK
//
//  Created by jujien on 05/09/2022.
//

import Foundation

public enum ConnectType: String {
  case connect = "connect"
  case transfer = "transfer"
  case depositToken = "deposit_token"
  case withdrawToken = "withdraw_token"
  case approve = "approve"
  case sendNFT = "send_nft"
  case buyNFT = "buy_nft"
  case sellNFT = "sell_nft"
  case buyBox = "buy_box"
  case unlockBox = "unlock_box"
  case openBox = "open_box"
  case sendBox = "send_box"
  case stakeNFT = "stake_nft"
  case unstakeNFT = "unstake_nft"
  case dismiss = "cancel_transaction"
  case cancelTransaction = "cancel_transaction_non_success"
}
