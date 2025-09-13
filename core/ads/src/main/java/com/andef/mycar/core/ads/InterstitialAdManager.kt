package com.andef.mycar.core.ads

import android.app.Activity
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.AdTheme
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader

class InterstitialAdManager(
    private val activity: Activity,
    private val id: String
) {
    private var interstitialAd: InterstitialAd? = null
    private var interstitialAdLoader: InterstitialAdLoader? = null
    private var isLightTheme: Boolean = true

    fun setLightTheme(isLightTheme: Boolean) {
        this.isLightTheme = isLightTheme
    }

    init {
        interstitialAdLoader = InterstitialAdLoader(activity).apply {
            setAdLoadListener(object : InterstitialAdLoadListener {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    this@InterstitialAdManager.interstitialAd = interstitialAd
                }

                override fun onAdFailedToLoad(error: AdRequestError) {}
            })
        }
        loadAd()
    }

    fun loadAd() {
        val adRequestConfiguration = AdRequestConfiguration.Builder(id)
            .setPreferredTheme(if (isLightTheme) AdTheme.LIGHT else AdTheme.DARK)
            .setContextTags(contextTags)
            .build()
        interstitialAdLoader?.loadAd(adRequestConfiguration)
    }


    fun showAd(afterShow: () -> Unit) {
        interstitialAd?.apply {
            setAdEventListener(object : InterstitialAdEventListener {
                override fun onAdShown() {}
                override fun onAdFailedToShow(adError: AdError) {
                    cleanup()
                    afterShow()
                }

                override fun onAdDismissed() {
                    cleanup()
                    afterShow()
                }

                override fun onAdClicked() {}
                override fun onAdImpression(impressionData: ImpressionData?) {}
            })
            show(activity)
        } ?: run {
            afterShow()
        }
    }

    private fun cleanup() {
        interstitialAd?.setAdEventListener(null)
        interstitialAd = null
        loadAd()
    }

    fun destroy() {
        interstitialAdLoader?.setAdLoadListener(null)
        interstitialAdLoader = null
        interstitialAd?.setAdEventListener(null)
        interstitialAd = null
    }
}

private val contextTags = listOf(
    "автомобиль",
    "бюджет",
    "уход за автомобилем",
    "автосервис",
    "автозапчасти",
    "автострахование",
    "автокредит",
    "автосалоны"
)