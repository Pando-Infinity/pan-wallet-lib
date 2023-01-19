import React, { useEffect, useRef } from "react";
import { View } from 'react-native';
import { NativeModules, NativeEventEmitter } from 'react-native';
import error from "./src/error";
import { supportCoin } from "./src/balance/balance";
import { eventType } from "./src/constants";
import { Balance } from "./src/balance";
const App = () => {
  useEffect(async () => {
    var config = {
      mode: "release",
      infuraKey: ""
    }
    var balance = new Balance(config)
    const event = new NativeEventEmitter(NativeModules.PanWallet)
    event.addListener(eventType.set_up_service, val => {
      balance = new Balance(val)
    })
    event.addListener(eventType.get_balance, val => {
      switch (val.coin) {
        case supportCoin.binanceSmartChain:
          balance.binanceSmartChain(val.address).then(ans => {
            NativeModules.PanWallet.reciveBalance(`${ans}`, "")
          }).catch(err => {
            NativeModules.PanWallet.reciveBalance("", err)
          })
          break;
        case supportCoin.bitcoin:
          balance.bitcoin(val.address)
            .then(ans => NativeModules.PanWallet.reciveBalance(`${ans}`, ""))
            .catch(err => NativeModules.PanWallet.reciveBalance("", err))
          break;
        case supportCoin.ethereum:
          balance.ethereum(val.address)
            .then(ans => NativeModules.PanWallet.reciveBalance(`${ans}`, ""))
            .catch(err => NativeModules.PanWallet.reciveBalance("", err))
          break;
        case supportCoin.solana:
          balance.solana(val.address)
            .then(ans => NativeModules.PanWallet.reciveBalance(`${ans}`, ""))
            .catch(err => NativeModules.PanWallet.reciveBalance("", err))
          break;
        default:
          NativeModules.PanWallet.reciveBalance("", error.coin_not_support)
          break;
      }
    })
  })
  return (<View style={{ flex: 1 }}></View>)
}
export default App;