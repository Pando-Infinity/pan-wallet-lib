//
//  NFT.swift
//  PanWalletSDK
//
//  Created by jujien on 10/08/2022.
//

import Foundation

public struct NFT: Codable {
  public var id: String
  public var image: String
  public var name: String
  public var price: Double?
  
  public init(id: String, image: String, name: String, price: Double? = nil) {
    self.id = id
    self.image = image
    self.name = name
    self.price = price
  }
}
