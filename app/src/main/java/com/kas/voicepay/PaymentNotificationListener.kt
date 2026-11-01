package com.kas.voicepay

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.speech.tts.TextToSpeech
import java.util.Locale

class PaymentNotificationListener : NotificationListenerService() {

    private var tts: TextToSpeech? = null

    override fun onCreate() {
        super.onCreate()
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
            }
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val packageName = sbn.packageName ?: return

        val supportedApps = listOf(
            "com.phonepe.app",
            "com.google.android.apps.nbu.paisa.user",
            "net.one97.paytm",
            "in.org.npci.upiapp"
        )

        if (packageName !in supportedApps) return

        val extras = sbn.notification.extras ?: return
        val title = extras.getCharSequence("android.title")?.toString() ?: ""
        val message = extras.getCharSequence("android.text")?.toString() ?: ""

        val combined = "$title $message"
        val paymentKeywords = listOf(
            "received", "credited", "payment received",
            "பணம் வந்தது", "பெறப்பட்டது"
        )

        if (paymentKeywords.none {
                combined.contains(it, ignoreCase = true)
            }) return

        val amountRegex = Regex(
            """(?:₹|Rs\.?\s*)([0-9,]+(?:\.[0-9]{1,2})?)""",
            RegexOption.IGNORE_CASE
        )

        val amount = amountRegex.find(combined)?.groupValues?.get(1)

        val announcement = if (amount != null) {
            "Payment notification received. Amount $amount rupees. Please verify in your bank app."
        } else {
            "A possible payment notification was received. Please verify in your bank app."
        }

        tts?.speak(
            announcement,
            TextToSpeech.QUEUE_ADD,
            null,
            sbn.key
        )
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}
