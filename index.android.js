/**
 * @format
 */
import './global'
import 'react-native-get-random-values'
import 'react-native-url-polyfill/auto'
import '@ethersproject/shims'
import { AppRegistry } from "react-native";
import App from "./App2";
import { name as appName } from "./app.json";

AppRegistry.registerComponent(appName, () => App)
