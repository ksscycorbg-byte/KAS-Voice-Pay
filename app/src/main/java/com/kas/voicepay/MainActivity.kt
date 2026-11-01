package com.kas.voicepay

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.view.Gravity
import android.graphics.Color
import android.view.ViewGroup
import java.util.Locale

class MainActivity : Activity(), TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isTamil = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tts = TextToSpeech(this, this)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(32, 40, 32, 40)
            setBackgroundColor(Color.rgb(12, 20, 35))
        }

        val title = TextView(this).apply {
            text = "KAS VOICE PAY"
            textSize = 30f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }

        val subtitle = TextView(this).apply {
            text = "Smart Payment Voice Assistant"
            textSize = 16f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
        }

        val accessButton = makeButton("Enable Notification Access")
        accessButton.setOnClickListener {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }

        val languageButton = makeButton("Language: English")
        languageButton.setOnClickListener {
            isTamil = !isTamil
            languageButton.text =
                if (isTamil) "Language: Tamil" else "Language: English"
            tts?.language = if (isTamil) Locale("ta", "IN") else Locale.US
        }

        val testButton = makeButton("Test Voice Announcement")
        testButton.setOnClickListener {
            val message = if (isTamil)
                "கே ஏ எஸ் வாய்ஸ் பேக்கு வரவேற்கிறோம்"
            else
                "Welcome to KAS Voice Pay"
            tts?.speak(message, TextToSpeech.QUEUE_FLUSH, null, "test")
        }

        val info = TextView(this).apply {
            text = "\nPayment announcements\n\nYour payment notifications will appear here.\n\nNote: Always verify payments in your UPI or bank app."
            textSize = 16f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }

        layout.addView(title)
        layout.addView(subtitle)
        layout.addView(accessButton)
        layout.addView(languageButton)
        layout.addView(testButton)
        layout.addView(info)

        setContentView(layout)
    }

    private fun makeButton(label: String): Button {
        return Button(this).apply {
            text = label
            textSize = 15f
            isAllCaps = false
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.rgb(20, 130, 100))
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 18, 0, 0)
            layoutParams = params
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.US
        }
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}
