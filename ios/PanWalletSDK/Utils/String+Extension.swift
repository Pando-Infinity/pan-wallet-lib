//
//  String+Extension.swift
//  PanWalletSDK
//
//  Created by jujien on 05/09/2022.
//

import Foundation
import UIKit

extension String {
  func verifyUrl () -> Bool {
    if let url = URL(string: self) {
        return UIApplication.shared.canOpenURL(url)
    }
    return false
  }
  
  internal static var pan_access_token : String {"pan_access_token"}
}
