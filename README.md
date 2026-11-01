# KAS Voice Pay — Android starter project

This is a starter Android project, not a finished or verified payment product.

## What is included
- Native Android home screen
- English/Tamil text-to-speech test
- Voice on/off and volume settings
- Notification-listener permission entry point
- Initial UPI notification listener for the requested apps

## Important limitations
- Notification wording and package IDs can change. BHIM package ID in particular must be confirmed on the target phone.
- Payment text parsing is deliberately conservative and will miss some real payments; it may still misclassify unusual notification wording.
- A notification is not proof of bank settlement. Verify money in the bank/UPI app.
- Female voice depends on the Android device's installed TTS engine and voice packs.
- This prototype does not yet implement a persistent payment ledger or an exhaustive audited set of payment-notification rules.
- Do not use it as the sole basis for handing over goods/services.
- Notification access exposes notification content from selected apps to this app. Only enable it if you trust the app and understand the privacy implications.

## Build APK using GitHub Actions
1. Create a private GitHub repository and upload the contents of this folder.
2. Open the repository's Actions tab.
3. Run the workflow "Build KAS Voice Pay APK".
4. Download the `KASVoicePay-debug-apk` artifact from the completed workflow.
5. Install the APK on your Android phone and grant notification access in Android Settings.

The workflow builds a debug APK for testing. A Play Store release requires a properly signed release build and additional testing/review.

## Build locally
Requires JDK 17, Android SDK platform 35 and Gradle 8.9:
`gradle assembleDebug`
