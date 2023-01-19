import React from 'react'
import { } from 'react-native'
import { Balance } from './src/balance'
import "react-native-url-polyfill/auto";

const App = () => {
    var config = {
        mode: "release",
        infuraKey: "6ef407ba8dde4bb09bc1699b2f27702b"
    }
    React.useEffect(() => {
        const balance = new Balance(config)
        balance.ethereum("0xa5254e3E5c3dEc8Ee1e3FC983716B17139b3CF55").then(res => { console.log("eth", res) }).catch(error => console.log(error))
        balance.bitcoin("bc1qgdjqv0av3q56jvd82tkdjpy7gdp9ut8tlqmgrpmv24sq90ecnvqqjwvw97").then(res => console.log("btc", res)).catch(err => console.log("as", err))
        balance.binanceSmartChain("0xa5254e3E5c3dEc8Ee1e3FC983716B17139b3CF55").then(res => console.log("bsc", res)).catch(err => console.log(err))
        balance.solana("HHNSz5t9K5hV2HJJjE53vbvv4B5HWT2iRBxLBbqwsoiK").then(res => console.log("sol", res)).catch(err => console.log("sol", err))
    })
    return (<></>)
}

export default App