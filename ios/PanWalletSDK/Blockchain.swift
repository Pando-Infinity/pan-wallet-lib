//
//  PanWallet.swift
//  PanWalletSDK
//
//  Created by jujien on 19/05/2022.
//

import Foundation

public enum Blockchain : String, CaseIterable {
  case multichain = "multichain"
  case bitcoin = "btc"
  case ethereum = "eth"
  case binanceSmartChain = "bsc"
  case solana = "sol"
}
