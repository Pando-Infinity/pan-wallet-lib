//
//  PanWalletEvent.swift
//  PanWalletSDK
//
//  Created by jujien on 19/05/2022.
//

import Foundation
import UIKit

public class PanWalletManager {
  public static let shared = PanWalletManager()

  var config : Config?

  public var currentWalletNetwork: Blockchain?

  private init() {}

  public func setConfig(config: Config) {

    if config.dappName.isEmpty {
      fatalError(PanError.dappNameEmpty.description)
    }
    if config.dappScheme.isEmpty {
      fatalError("`dappScheme` must be not empty")
    }
    if config.dappUrl.isEmpty {
      fatalError("`dappUrl` must be not empty")
    }

    self.config = config
  }

  public func connect(chain: Blockchain) throws {
    guard let config = self.config else {
      fatalError(PanError.configNil.description)
    }
    let walletUrl = config.getConnectRequest(chain: chain)
    guard let url = walletUrl else {
      throw PanError.unknow
    }
    self.currentWalletNetwork = chain
    try self.open(url: url)
  }

  public func transferToken(chain: Blockchain, contractAddress: String?, addressReceive: String, amount: Double) throws {
    guard let config = self.config else {
      fatalError(PanError.configNil.description)
    }

    guard let walletChain = self.currentWalletNetwork else {
      fatalError("walletChain must set value")
    }

    guard self.isConnected else {
      throw PanError.notConnected
    }

    guard amount > 0 else {
      fatalError("`amount` must be a positive number and greater than 0")
    }

    guard !addressReceive.isEmpty else {
      fatalError("`addressReceive` must be not empty")
    }

    guard let url = config.getTransferRequest(walletChain: walletChain, chain: chain, contractAddress: contractAddress, addressReceive: addressReceive, amount: amount) else { return }
    try self.open(url: url)
  }

  public func approve(type: ApproveType, chain: Blockchain, contractAddress: String, tokenSymbol: String? = nil, approveAddress: ObjectAddress, transactionData: [String: Any], amount: String? = nil) throws {
    guard let config = self.config else {
      fatalError(PanError.configNil.description)
    }

    guard let walletChain = self.currentWalletNetwork else {
      fatalError("walletChain must set value")
    }

    guard !transactionData.isEmpty else {
      fatalError("`transactionData` must be not empty")
    }

    guard !contractAddress.isEmpty else {
      fatalError("`tokenSymbol` must be not empty")
    }

    guard type != .buyNFT || (tokenSymbol != nil && !tokenSymbol!.isEmpty) else {
      fatalError("`tokenSymbol` must be not empty")
    }

    if type == .depositToken || type == .unStakeNFT {
      guard let amount = amount, Double(amount)! > 0 else {
        fatalError("`amount` must be a positive number and greater than 0")
      }
    }


    switch approveAddress {
    case .spender(let address):
      if address.isEmpty  {
        fatalError("`approveAddress` must be not empty")
      }
    case .operator(let address):
      if address.isEmpty {
        fatalError("`approveAddress` must be not empty")
      }
    case .destination(_):
      fatalError("`approveAddress` failed")
    }

    guard self.isConnected else {
      throw PanError.notConnected
    }

    guard let url = try config.getApprove(type: type, walletChain: walletChain, chain: chain, contractAddress: contractAddress, tokenSymbol: tokenSymbol, amount: amount, approveAddress: approveAddress, transactionData: transactionData) else {
      return
    }
    try self.open(url: url)

  }

  public func depositToken(chain: Blockchain, contractAddress: String, amount: Double, addressSpender: String, transactionData: [String: Any]) throws {
    guard let config = self.config else {
      fatalError(PanError.configNil.description)
    }

    guard let walletChain = self.currentWalletNetwork else {
      fatalError("walletChain must set value")
    }

    guard !contractAddress.isEmpty else {
      fatalError("`tokenSymbol` must be not empty")
    }

    guard amount > 0 else {
      fatalError("`amount` must be a positive number and greater than 0")
    }

    guard !addressSpender.isEmpty else {
      fatalError("`to address` must be not empty")
    }

    guard !transactionData.isEmpty else {
      fatalError("`transactionData` must be not empty")
    }

    guard self.isConnected else {
      throw PanError.notConnected
    }

    guard let url = try config.requestDepositToken(walletChain: walletChain, chain: chain, contractAddress: contractAddress, addressSpender: addressSpender, amount: amount, transactionData: transactionData) else {
      return
    }
    try self.open(url: url)
  }

  public func withdrawToken(chain: Blockchain, contractAddress: String, amount: Double, transactionData: [String: Any]) throws {
    guard let config = self.config else {
      fatalError(PanError.configNil.description)
    }

    guard let walletChain = self.currentWalletNetwork else {
      fatalError("walletChain must set value")
    }

    guard !contractAddress.isEmpty else {
      fatalError("`tokenSymbol` must be not empty")
    }

    guard amount > 0 else {
      fatalError("`amount` must be a positive number and greater than 0")
    }

    guard !transactionData.isEmpty else {
      fatalError("`to address` must be not empty")
    }

    guard self.isConnected else {
      throw PanError.notConnected
    }

    guard let url = try config.requestWithdrawToken(walletChain: walletChain, chain: chain, contractAddress: contractAddress, transactionData: transactionData, amount: amount) else { return }

    try self.open(url: url)
  }

  public func sendNFT(chain: Blockchain, contractAddress: String, nft: NFT, to address: String, transactionData: [String: Any]) throws {
    try self.transferNFT(connectType: .sendNFT, chain: chain, tokenSymbol: nil, contractAddress: contractAddress, nft: nft, transactionData: transactionData, address: .destination(address))
  }

  public func sellNFT(chain: Blockchain, contractAddress: String, tokenSymbol: String, nft: NFT, transactionData: [String: Any], addressOperator: String) throws {
    try self.transferNFT(connectType: .sellNFT, chain: chain, tokenSymbol: tokenSymbol, contractAddress: contractAddress, nft: nft, transactionData: transactionData, address: .operator(addressOperator))
  }

  public func buyNFT(chain: Blockchain, contractAddress: String, tokenSymbol: String, nft: NFT, transactionData: [String: Any], addressSpender: String) throws {
    try self.transferNFT(connectType: .buyNFT, chain: chain, tokenSymbol: tokenSymbol, contractAddress: contractAddress, nft: nft, transactionData: transactionData, address: .spender(addressSpender))
  }

  public func stakeNFT(chain: Blockchain, contractAddress: String, nft: NFT, addressOperator: String, transactionData: [String: Any]) throws {
    try self.transferNFT(connectType: .stakeNFT, chain: chain, tokenSymbol: nil, contractAddress: contractAddress, nft: nft, transactionData: transactionData, address: .operator(addressOperator))
  }

  public func unstakeNFT(chain: Blockchain, contractAddress: String, nft: NFT, amount: String, addressSpender: String, transactionData: [String: Any]) throws {
    try self.transferNFT(connectType: .unstakeNFT, chain: chain, tokenSymbol: nil, contractAddress: contractAddress, nft: nft, transactionData: transactionData, address: .spender(addressSpender), amount: amount)
  }

  public func buyBox(chain: Blockchain, nameBox: String, contractAddress: String, addressOperator: String, transactionData: [String: Any]) throws {
    try self.transferBox(connectType: .buyBox, chain: chain, nameBox: nameBox, contractAddress: contractAddress, transactionData: transactionData, to: .operator(addressOperator))
  }

  public func unlockBox(chain: Blockchain, nameBox: String, contractAddress: String, addressOperator: String, transactionData: [String: Any]) throws {
    try self.transferBox(connectType: .unlockBox, chain: chain, nameBox: nameBox, contractAddress: contractAddress, transactionData: transactionData, to: .operator(addressOperator))
  }

  public func openBox(chain: Blockchain, nameBox: String, contractAddress: String, transactionData: [String: Any]) throws {
    try self.transferBox(connectType: .openBox, chain: chain, nameBox: nameBox, contractAddress: contractAddress, transactionData: transactionData, to: nil)
  }

  public func sendBox(chain: Blockchain, nameBox: String, contractAddress: String, to address: String, transactionData: [String: Any]) throws {
    try self.transferBox(connectType: .sendBox, chain: chain, nameBox: nameBox, contractAddress: contractAddress, transactionData: transactionData, to: .destination(address))
  }

  public func cancelTransaction(chain: Blockchain? = nil, contractAddress: String? = nil, transactionData: [String: Any] = [:]) throws {
    guard let config = self.config else {
      fatalError(PanError.configNil.description)
    }

    guard let walletChain = self.currentWalletNetwork else {
      fatalError("walletChain must set value")
    }

    if chain != nil || contractAddress != nil || !transactionData.isEmpty {
      if chain == nil {
        fatalError("`chain` must to set value")
      }
      if contractAddress == nil {
        fatalError("`contractAddress` must to set value")
      }
      if transactionData.isEmpty {
        fatalError("`transactionData` must be not empty")
      }
    }

    guard self.isConnected else {
      throw PanError.notConnected
    }
    guard let url = try config.requestCancelTransaction(walletChain: walletChain, chain: chain, contractAddress: contractAddress, transactionData: transactionData) else { return }
    try self.open(url: url)

  }

  public func convert(url: URL) throws -> PanResponse {
    if url.host == "panconnect" {
      let component = URLComponents(url: url, resolvingAgainstBaseURL: false)
      var data = component?.queryItems?.filter { $0.value != nil }.map { [$0.name: $0.value!] }.reduce([String:String](), { result, value in
        var dict = result
        value.forEach { key, value in
          dict[key] = value
        }
        return dict
      }) ?? [:]
      let responseType = ConnectType(rawValue: String(url.path.dropFirst())) ?? .connect
      if let accessToken = data[.pan_access_token] {
        if accessToken.split(separator: "-").count > 0 {
          UserDefaults.shared?.set(accessToken, forKey: String(accessToken.split(separator: "-")[0]))
        }
      }
      if data.contains(where: { $0.key == "address" }) {
        let address = data.removeValue(forKey: "address")
        if let results = try? JSONSerialization.jsonObject(with: address?.data(using: .utf8) ?? .init()) as? [String: String] {
          results.forEach { key, value in
            data[key] = value
          }
        }
      }

      return PanResponse(
        code: Int(data["code"] ?? "0") ?? 0,
        connectType: responseType,
        message: data["message"] ?? "",
        data: data.filter{
          return !($0.key == "code" || $0.key == "message" || $0.key == .pan_access_token)
        } )
    } else {
      throw PanError.notFromPan
    }
  }

  public func disconnect() throws {
    if let chain = self.currentWalletNetwork {
      if self.isConnected {
        UserDefaults.shared?.removeObject(forKey: chain.rawValue)
      } else {
        throw PanError.notConnected
      }
    } else {
      fatalError("Current network wallet not set")
    }

  }

}

extension PanWalletManager {

  var isConnected: Bool {
    let accessToken = UserDefaults.shared?.string(forKey: self.currentWalletNetwork?.rawValue ?? "")
    return accessToken != nil && accessToken! != ""
  }

  fileprivate func transferNFT(connectType: ConnectType, chain: Blockchain, tokenSymbol: String?, contractAddress: String, nft: NFT, transactionData: [String: Any], address: ObjectAddress?, amount: String? = nil) throws {
    guard let config = self.config else {
      fatalError(PanError.configNil.description)
    }

    guard let walletChain = self.currentWalletNetwork else {
      fatalError("walletChain must set value")
    }

    guard self.isConnected else {
      throw PanError.notConnected
    }

    guard !transactionData.isEmpty else {
      fatalError("`transactionData` must be not empty")
    }

    guard !contractAddress.isEmpty else {
      fatalError("`tokenSymbol` must be not empty")
    }

    guard !nft.id.isEmpty else {
      fatalError("`id nft` must be not empty")
    }

    guard !nft.name.isEmpty else {
      fatalError("`name nft` must be not empty")
    }

    if connectType == .buyNFT || connectType == .sellNFT {
      if nft.price == nil || nft.price! <= 0 {
        fatalError("`price nft` must be not empty")
      }
      guard let tokenSymbol = tokenSymbol, !tokenSymbol.isEmpty else {
        fatalError("`tokenSymbol` must be not empty")
      }
    }

    if connectType != .unstakeNFT || connectType != .openBox {
      switch address {
      case .operator(let op):
        if op.isEmpty {
          fatalError("`address` must be not empty")
        }
      case .spender(let sp):
        if sp.isEmpty {
          fatalError("`address` must be not empty")
        }
      case .destination(let des):
        if des.isEmpty {
          fatalError("`address` must be not empty")
        }
      case .none:
        fatalError("`address` must be not empty")
      }
    }

    guard let url = try config.requestTransferNft(walletChain: walletChain, chain: chain, connectType: connectType, tokenSymbol: tokenSymbol, contractAddress: contractAddress, nft: nft, transactionData: transactionData, address: address, amount: amount) else { return }

    let check = UIApplication.shared.canOpenURL(url)
    if check {
      UIApplication.shared.open(url)
    } else {
      throw PanError.notDownload
    }
  }

  fileprivate func transferBox(connectType: ConnectType, chain: Blockchain, nameBox: String, contractAddress: String, transactionData: [String: Any], to address: ObjectAddress?) throws {
    guard let config = self.config else {
      fatalError(PanError.configNil.description)
    }

    guard let walletChain = self.currentWalletNetwork else {
      fatalError("walletChain must set value")
    }

    guard !transactionData.isEmpty else {
      fatalError("`transactionData` must be not empty")
    }

    guard !contractAddress.isEmpty else {
      fatalError("`tokenSymbol` must be not empty")
    }

    guard nameBox.count <= 20 && !nameBox.isEmpty else {
      fatalError("`nameBox` must be 20 characters or less and cannot be null")
    }

    if connectType == .sendBox {
      switch address {
      case .none: fatalError("`toAddress` must be set value")
      case .operator(let op):
        if op.isEmpty {
          fatalError("`toAddress` must be set value")
        }
      case .destination(let des):
        if des.isEmpty {
          fatalError("`toAddress` must be set value")
        }
      default:
        fatalError("not support")
      }
    }

    guard self.isConnected else {
      throw PanError.notConnected
    }

    guard let url = try config.requestTransferBox(type: connectType, walletChain: walletChain, chain: chain, contractAddress: contractAddress, nameBox: nameBox, toAddress: address, transactionData: transactionData) else { return }
    try self.open(url: url)
  }

  fileprivate func open(url: URL) throws {
    let check = UIApplication.shared.canOpenURL(url)
    if check {
      UIApplication.shared.open(url)
    } else {
      throw PanError.notDownload
    }
  }

}


