# PanWallet-SDK



## About PanWallet-SDK
Pan Wallet SDK also provide libraries for Dapps to easily connect and interact with Pan Wallet. 


## Requirements
- Common:
    - Nodejs
- iOS:
    - iOS 14 and above
    - Xcode 12 and above
-  Android:
    - Android 10 and above
    - Android Studio 3.0 and above

## Build Library
### First
Run the following command:
```sh
# Install dependencies
yarn install

# For IOS:
yarn install
cd ./ios && pod install
```
### iOS
You open the `PanWallet.xcworkspace` from the ios folder.
Make sure the `PanWalletSDK` target it selected, and press CMD + B or tap Build button to build it.
When build done, go to `Product` -> `Show Build Folder in Finder`. 
When Finder is open, go to `Product` -> `Release-iphoneos` -> copy `PanWalletSDK.framework`.
### Android
You open folder `android` from `Android Studio` and tap `Build` to build it.
When build is done, go to `PanWalletSDK` -> `build` -> `outputs` -> `aar`. copy `PanWalletSDK-debug.aar` or `PanWalletSDK-release.aar`

## Integrating the Library
### iOS
1. Copy `PanWalletSDK.framework` to your project.
2. Select your target and go to `General`. In the `Frameworks and Libraries`, make sure `PanWalletSDK.framework` with embeded is `Embeded & Signin`
### Android
1. Create `libs` folder under src/main
2. Copy `PanWalletSDK-release.aar`/`PanWalletSDK-debug.aar` file to src/main/libs
Open `build.gradle` file(the one under `app`) and add dependency `compile(name:'PanWalletSDK-release', ext:'aar')`/`compile(name:'PanWalletSDK-debug', ext:'aar')`
