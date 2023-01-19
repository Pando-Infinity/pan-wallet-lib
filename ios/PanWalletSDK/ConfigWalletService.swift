//
//  ConfigWalletService.swift
//  PanWalletSDK
//
//  Created by Hoang Hiep on 26/05/2022.
//

import Foundation

public struct Config {
  let dappName: String
  let dappScheme: String
  let dappLogo: String?
  let dappUrl: String

  public init(dappName: String, dappScheme:String, dappUrl: String, dappLogo: String?) {
    self.dappName = dappName
    self.dappScheme = dappScheme
    self.dappLogo = dappLogo
    self.dappUrl = dappUrl
  }
}

extension Config {
  func getConnectRequest(chain: Blockchain) -> URL? {
    var components = URLComponents()
    components.scheme = "panwallet"
    components.host = "pan"
    components.path = "/" + ConnectType.connect.rawValue

    let access_token = UserDefaults.shared?.string(forKey: chain.rawValue) ?? ""
    components.queryItems = [
      URLQueryItem(name: "name", value: self.dappName),
      URLQueryItem(name: "scheme", value: self.dappScheme + "://"),
      URLQueryItem(name: "logo", value: self.dappLogo ?? ""),
      URLQueryItem(name: "chain", value: chain.rawValue),
      URLQueryItem(name: "url", value: self.dappUrl),
      URLQueryItem(name: .pan_access_token, value: access_token),
      URLQueryItem(name: "bundle", value: Bundle.main.bundleIdentifier ?? "")
    ].filter{$0.value != ""}
    return components.url
  }

  func getTransferRequest(walletChain: Blockchain, chain: Blockchain, contractAddress: String?, addressReceive: String, amount: Double) -> URL? {
    let access_token = UserDefaults.shared?.string(forKey: walletChain.rawValue) ?? ""
    var components = URLComponents()
    components.scheme = "panwallet"
    components.host = "pan"
    components.path = "/" + ConnectType.transfer.rawValue
    components.queryItems = [
      .init(name: "name", value: self.dappName),
      .init(name: "scheme", value: self.dappScheme + "://"),
      .init(name: .pan_access_token, value: access_token),
      .init(name: "amount", value: "\(amount)"),
      .init(name: "address_receive", value: addressReceive),
      .init(name: "chain", value: chain.rawValue),
      .init(name: "bundle", value: Bundle.main.bundleIdentifier ?? "")
    ]
    if let contractAddress = contractAddress {
      components.queryItems?.append(.init(name: "contract_address", value: contractAddress))
    }
    return components.url
  }

  func getApprove(type: ApproveType, walletChain: Blockchain, chain: Blockchain, contractAddress: String, tokenSymbol: String?, amount: String?, approveAddress: ObjectAddress, transactionData: [String: Any]) throws -> URL? {
    let access_token = UserDefaults.shared?.string(forKey: walletChain.rawValue) ?? ""

    var components = URLComponents()
    components.scheme = "panwallet"
    components.host = "pan"
    components.path = "/" + ConnectType.approve.rawValue
    components.queryItems = [
      .init(name: "scheme", value: self.dappScheme + "://"),
      .init(name: "bundle", value: Bundle.main.bundleIdentifier ?? ""),
      .init(name: "type", value: type.rawValue),
      .init(name: .pan_access_token, value: access_token),
      .init(name: "contract_address", value: contractAddress),
      .init(name: "chain", value: chain.rawValue),
    ]
    if let amount = amount {
      components.queryItems?.append(.init(name: "amount", value: amount))
    }
    if let tokenSymbol = tokenSymbol {
      components.queryItems?.append(.init(name: "token_symbol", value: tokenSymbol))
    }
    if !transactionData.isEmpty {
      let data = try JSONSerialization.data(withJSONObject: transactionData, options: [])
      let string = String(data: data, encoding: .utf8) ?? ""
      components.queryItems?.append(.init(name: "transaction_data", value: string))
    }
    
    switch approveAddress {
    case .operator(let address):
      components.queryItems?.append(.init(name: "address_operator", value: address))
    case .spender(let address):
      components.queryItems?.append(.init(name: "address_spender", value: address))
    default: break 
    }
    return components.url
  }

  func requestDepositToken(walletChain: Blockchain, chain: Blockchain, contractAddress: String, addressSpender: String, amount: Double, transactionData: [String: Any]) throws -> URL? {
    let access_token = UserDefaults.shared?.string(forKey: walletChain.rawValue) ?? ""
    let data = try JSONSerialization.data(withJSONObject: transactionData, options: [])
    let string = String(data: data, encoding: .utf8) ?? ""

    var components = URLComponents()
    components.scheme = "panwallet"
    components.host = "pan"
    components.path = "/" + ConnectType.depositToken.rawValue
    components.queryItems = [
      .init(name: "scheme", value: self.dappScheme + "://"),
       .init(name: "bundle", value: Bundle.main.bundleIdentifier ?? ""),
      .init(name: .pan_access_token, value: access_token),
      .init(name: "contract_address", value: contractAddress),
      .init(name: "chain", value: chain.rawValue),
      .init(name: "amount", value: "\(amount)"),
      .init(name: "address_spender", value: addressSpender),
      .init(name: "transaction_data", value: string)
    ]
    return components.url
  }

  func requestWithdrawToken(walletChain: Blockchain, chain: Blockchain, contractAddress: String, transactionData: [String: Any], amount: Double) throws -> URL? {
    let access_token = UserDefaults.shared?.string(forKey: walletChain.rawValue) ?? ""
    let data = try JSONSerialization.data(withJSONObject: transactionData, options: [])
    let string = String(data: data, encoding: .utf8) ?? ""

    var components = URLComponents()
    components.scheme = "panwallet"
    components.host = "pan"
    components.path = "/" + ConnectType.withdrawToken.rawValue
    components.queryItems = [
      .init(name: "scheme", value: self.dappScheme + "://"),
      .init(name: "bundle", value: Bundle.main.bundleIdentifier ?? ""),
      .init(name: .pan_access_token, value: access_token),
      .init(name: "contract_address", value: contractAddress),
      .init(name: "chain", value: chain.rawValue),
      .init(name: "amount", value: "\(amount)"),
      .init(name: "transaction_data", value: string)
    ]
    return components.url
  }

  func requestTransferNft(walletChain: Blockchain, chain: Blockchain, connectType: ConnectType, tokenSymbol: String?, contractAddress: String, nft: NFT, transactionData: [String: Any], address: ObjectAddress?, amount: String?) throws -> URL? {
    let access_token = UserDefaults.shared?.string(forKey: walletChain.rawValue) ?? ""
    var components = URLComponents()
    components.scheme = "panwallet"
    components.host = "pan"
    components.path = "/" + connectType.rawValue

    let encoder = JSONEncoder()
    let nftData = try encoder.encode(nft)
    let nftJson = String(data: nftData, encoding: .utf8) ?? ""

    let data = try JSONSerialization.data(withJSONObject: transactionData, options: [])
    let string = String(data: data, encoding: .utf8) ?? ""

    components.queryItems = [
      .init(name: "bundle", value: Bundle.main.bundleIdentifier ?? ""),
      .init(name: "scheme", value: self.dappScheme + "://"),
      .init(name: "scheme", value: self.dappScheme),
      .init(name: "chain", value: chain.rawValue),
      .init(name: .pan_access_token, value: access_token),
      .init(name: "contract_address", value: contractAddress),
      .init(name: "nft", value: nftJson),
      .init(name: "transaction_data", value: string)
    ]
    if let tokenSymbol = tokenSymbol {
      components.queryItems?.append(.init(name: "token_symbol", value: tokenSymbol))
    }
    if let amount = amount {
      components.queryItems?.append(.init(name: "amount", value: amount))
    }
    switch address {
    case .destination(let des):
      components.queryItems?.append(.init(name: "to_address", value: des))
    case .spender(let spender):
      components.queryItems?.append(.init(name: "address_spender", value: spender))
    case .operator(let `operator`):
      components.queryItems?.append(.init(name: "address_operator", value: `operator`))
    case .none: break
    }
    return components.url
  }

  func requestUnlock(walletChain: Blockchain, chain: Blockchain, contractAddress: String, amount: Double, transactionData: [String: Any]) throws -> URL? {
    let access_token = UserDefaults.shared?.string(forKey: walletChain.rawValue) ?? ""

    let data = try JSONSerialization.data(withJSONObject: transactionData, options: [])
    let string = String(data: data, encoding: .utf8) ?? ""

    var components = URLComponents()
    components.scheme = "panwallet"
    components.host = "pan"
    components.path = "/" + ConnectType.unlockBox.rawValue
    components.queryItems = [
      .init(name: "scheme", value: self.dappScheme + "://"),
      .init(name: "bundle", value: Bundle.main.bundleIdentifier ?? ""),
      .init(name: .pan_access_token, value: access_token),
      .init(name: "contract_address", value: contractAddress),
      .init(name: "chain", value: chain.rawValue),
      .init(name: "transaction_data", value: string),
      .init(name: "amount", value: "\(amount)")
    ]

    return components.url
  }

  func requestTransferBox(type: ConnectType, walletChain: Blockchain, chain: Blockchain, contractAddress: String, nameBox: String, toAddress: ObjectAddress?, transactionData: [String: Any]) throws -> URL? {
    let access_token = UserDefaults.shared?.string(forKey: walletChain.rawValue) ?? ""

    let data = try JSONSerialization.data(withJSONObject: transactionData, options: [])
    let string = String(data: data, encoding: .utf8) ?? ""

    var components = URLComponents()
    components.scheme = "panwallet"
    components.host = "pan"
    components.path = "/" + type.rawValue
    components.queryItems = [
      .init(name: "scheme", value: self.dappScheme + "://"),
      .init(name: "bundle", value: Bundle.main.bundleIdentifier ?? ""),
      .init(name: .pan_access_token, value: access_token),
      .init(name: "contract_address", value: contractAddress),
      .init(name: "chain", value: chain.rawValue),
      .init(name: "transaction_data", value: string),
      .init(name: "name_box", value: "\(nameBox)"),
      .init(name: "transaction_data", value: string)
    ]

    switch toAddress {
    case .destination(let des):
      components.queryItems?.append(.init(name: "to_address", value: des))
    case .spender(let spender):
      components.queryItems?.append(.init(name: "address_spender", value: spender))
    case .operator(let `operator`):
      components.queryItems?.append(.init(name: "address_operator", value: `operator`))
    case .none: break
    }

    return components.url
  }

  func requestCancelTransaction(walletChain: Blockchain, chain: Blockchain?, contractAddress: String?, transactionData: [String: Any]) throws -> URL? {
    let access_token = UserDefaults.shared?.string(forKey: walletChain.rawValue) ?? ""
    var components = URLComponents()
    components.scheme = "panwallet"
    components.host = "pan"
    components.path = "/" + ConnectType.dismiss.rawValue
    
    components.queryItems = [
      .init(name: .pan_access_token, value: access_token),
      .init(name: "scheme", value: self.dappScheme + "://"),
      .init(name: "bundle", value: Bundle.main.bundleIdentifier ?? "")
    ]
    
    if let chain = chain, let contractAddress = contractAddress, !transactionData.isEmpty {
      let data = try JSONSerialization.data(withJSONObject: transactionData, options: [])
      let string = String(data: data, encoding: .utf8) ?? ""
      components.path = "/" + ConnectType.cancelTransaction.rawValue
      components.queryItems?.append(contentsOf: [
        .init(name: "chain", value: chain.rawValue),
        .init(name: "contract_address", value: contractAddress),
        .init(name: "transaction_data", value: string)
      ])
    }
    return components.url
  }

}
