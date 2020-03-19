package com.example.systemengineview

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import mozilla.components.browser.engine.system.SystemEngine
import mozilla.components.browser.session.Session
import mozilla.components.browser.session.SessionManager
import mozilla.components.browser.state.store.BrowserStore
import mozilla.components.concept.engine.DefaultSettings
import mozilla.components.concept.engine.EngineSession

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        app.engine.warmUp()

        val engineSession = app.engine.createSession()
        engineView.render(engineSession)

        app.sessionManager.add(Session("https://instagram.com"), true, engineSession)
    }

    override fun onBackPressed() {
        if (!app.sessionManager.onBackPressed()) super.onBackPressed()
    }
}

class App : Application() {
    val engine: SystemEngine by lazy {
        SystemEngine(
            this, DefaultSettings(
                javascriptEnabled = true,
                requestInterceptor = AppRequestInterceptor(
                    this@App
                ),
                trackingProtectionPolicy = EngineSession.TrackingProtectionPolicy.recommended()
            )
        )
    }
    val sessionManager: SessionManager by lazy { SessionManager(engine, BrowserStore()) }
}
