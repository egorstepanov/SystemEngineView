/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.example.systemengineview

import android.content.Context
import android.util.Log
import mozilla.components.browser.engine.system.SystemEngineSession
import mozilla.components.browser.errorpages.ErrorPages
import mozilla.components.browser.errorpages.ErrorType
import mozilla.components.concept.engine.EngineSession
import mozilla.components.concept.engine.request.RequestInterceptor

class AppRequestInterceptor(private val context: Context) : RequestInterceptor {

    override fun onLoadRequest(
        engineSession: EngineSession,
        uri: String,
        hasUserGesture: Boolean,
        isSameDomain: Boolean
    ): RequestInterceptor.InterceptionResponse? {
        Log.i("URL", uri)
        return super.onLoadRequest(engineSession, uri, hasUserGesture, isSameDomain)
    }

    override fun onErrorRequest(
        session: EngineSession,
        errorType: ErrorType,
        uri: String?
    ): RequestInterceptor.ErrorResponse? {
        val improvedErrorType = improveErrorType(errorType)
        val imageNeeded = false

        val errorPage = if (imageNeeded) {
            val riskLevel = getRiskLevel(improvedErrorType)
            ErrorPages.createUrlEncodedErrorPage(
                context = context,
                errorType = improvedErrorType,
                uri = uri,
                htmlResource = riskLevel.htmlRes
            ).replace("resource://android/assets", "file:///android_asset")
        } else {
            ErrorPages.createErrorPage(context, improvedErrorType, uri)
        }

//        return if (imageNeeded) RequestInterceptor.ErrorResponse.Uri(errorPage)
//        else RequestInterceptor.ErrorResponse.Content(errorPage)

        // Workaround:
        // For url based error page we need do window.history.back() on click button;
        // For content based error page we need add historyUrl;
        // And return null.
        return (context.app.sessionManager.getEngineSession() as SystemEngineSession).webView.apply {
            if (imageNeeded) loadUrl(errorPage)
            else loadDataWithBaseURL(uri, errorPage, "text/html", "UTF8", uri)
        }.run {
            null
        }
    }

    /**
     * Where possible, this will make the error type more accurate by including information not
     * available to AC.
     */
    private fun improveErrorType(errorType: ErrorType): ErrorType {
        // This is not an ideal solution. For context, see:
        // https://github.com/mozilla-mobile/android-components/pull/5068#issuecomment-558415367

        val isConnected: Boolean = context.isConnected()

        return when {
            errorType == ErrorType.ERROR_UNKNOWN_HOST && !isConnected -> ErrorType.ERROR_NO_INTERNET
            else -> errorType
        }
    }

    private fun getRiskLevel(errorType: ErrorType): RiskLevel = when (errorType) {
        ErrorType.UNKNOWN,
        ErrorType.ERROR_NET_INTERRUPT,
        ErrorType.ERROR_NET_TIMEOUT,
        ErrorType.ERROR_CONNECTION_REFUSED,
        ErrorType.ERROR_UNKNOWN_SOCKET_TYPE,
        ErrorType.ERROR_REDIRECT_LOOP,
        ErrorType.ERROR_OFFLINE,
        ErrorType.ERROR_NET_RESET,
        ErrorType.ERROR_UNSAFE_CONTENT_TYPE,
        ErrorType.ERROR_CORRUPTED_CONTENT,
        ErrorType.ERROR_CONTENT_CRASHED,
        ErrorType.ERROR_INVALID_CONTENT_ENCODING,
        ErrorType.ERROR_UNKNOWN_HOST,
        ErrorType.ERROR_MALFORMED_URI,
        ErrorType.ERROR_FILE_NOT_FOUND,
        ErrorType.ERROR_FILE_ACCESS_DENIED,
        ErrorType.ERROR_PROXY_CONNECTION_REFUSED,
        ErrorType.ERROR_UNKNOWN_PROXY_HOST,
        ErrorType.ERROR_NO_INTERNET,
        ErrorType.ERROR_UNKNOWN_PROTOCOL -> RiskLevel.Low

        ErrorType.ERROR_SECURITY_BAD_CERT,
        ErrorType.ERROR_SECURITY_SSL,
        ErrorType.ERROR_PORT_BLOCKED -> RiskLevel.Medium

        ErrorType.ERROR_SAFEBROWSING_HARMFUL_URI,
        ErrorType.ERROR_SAFEBROWSING_MALWARE_URI,
        ErrorType.ERROR_SAFEBROWSING_PHISHING_URI,
        ErrorType.ERROR_SAFEBROWSING_UNWANTED_URI -> RiskLevel.High
    }

    internal enum class RiskLevel(val htmlRes: String) {
        Low(LOW_AND_MEDIUM_RISK_ERROR_PAGES),
        Medium(LOW_AND_MEDIUM_RISK_ERROR_PAGES),
        High(HIGH_RISK_ERROR_PAGES),
    }

    companion object {
        internal const val LOW_AND_MEDIUM_RISK_ERROR_PAGES = "low_and_medium_risk_error_pages.html"
        internal const val HIGH_RISK_ERROR_PAGES = "high_risk_error_pages.html"
    }
}
