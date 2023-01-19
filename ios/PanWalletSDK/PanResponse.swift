//
//  PanResponse.swift
//  PanWalletSDK
//
//  Created by Hoang Hiep on 27/05/2022.
//

import Foundation
public struct PanResponse {
  
  public var code : Int

  public var connectType : ConnectType

  public var message : String

  public var data : [String : String]
}
