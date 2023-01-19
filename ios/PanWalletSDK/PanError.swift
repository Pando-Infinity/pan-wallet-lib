//
//  ErrorMessage.swift
//  PanWalletSDK
//
//  Created by Hoang Hiep on 26/05/2022.
//

import Foundation
public struct PanError : Error {
  
  public var code : Int
  public var description: String
  var name : String {
    switch code {
    case ErrorType.notDownloaded.rawValue:
      return "Pan wallet not downloaded"
    case ErrorType.configFailed.rawValue:
      return "Please use `setConfig` before"
    default:
      return "unknow"
    }

  }
}

enum ErrorType : Int {
  case notDownloaded = 200
  case notFromPan = 201
  case configFailed = 300
  case schemaError = 302
  case logoDappError = 303
  case debugConfigNil = 304
  case dappNameEmpty = 305
  case notConnected = 306
}

extension PanWalletManager {
  var notConfig : String {"Please use `setConfig` before"}
}

extension PanError {
  static let notDownload = PanError(code: ErrorType.notDownloaded.rawValue, description: "Pan wallet has not been installed")

  static let configNil = PanError(code: ErrorType.configFailed.rawValue, description: "Please use `setConfig` before")

  static let unknow = PanError(code: 0, description: "unknow")

  static let notFromPan = PanError(code : 201, description: "Response is not from Pan Wallet")

  static let schemaError = PanError(code: ErrorType.schemaError.rawValue, description: "`dappScheme` must be url")

  static let logoUrlError = PanError(code: ErrorType.logoDappError.rawValue, description: "`logoDapp` must be url")

  static let debugConfigNil = PanError(code: ErrorType.debugConfigNil.rawValue, description: "debug config not nil if mode is debug")

  static var dappNameEmpty = PanError(code: ErrorType.dappNameEmpty.rawValue, description: "`name` of dapp is empty")
  
  static let notConnected = PanError(code: 306, description: "not connected to `PanWallet`")
}
