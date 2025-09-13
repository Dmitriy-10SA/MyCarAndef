package com.andef.mycarandef

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.nativeads.NativeAd
import com.yandex.mobile.ads.nativeads.NativeAdLoadListener
import com.yandex.mobile.ads.nativeads.NativeAdLoader
import com.yandex.mobile.ads.nativeads.NativeAdRequestConfiguration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainDrawerSheetContentAdsViewModel(private val application: Application) :
    AndroidViewModel(application) {
    private var nativeAdLoader: NativeAdLoader? = null

    private val _adView = MutableStateFlow<NativeAd?>(null)
    val adView: StateFlow<NativeAd?> = _adView.asStateFlow()

    private fun createNativeAdLoader(): NativeAdLoader {
        return nativeAdLoader ?: NativeAdLoader(application).apply {
            setNativeAdLoadListener(object : NativeAdLoadListener {
                override fun onAdFailedToLoad(error: AdRequestError) {
                }

                override fun onAdLoaded(nativeAd: NativeAd) {
                    _adView.value = nativeAd
                }
            })
        }
    }

    init {
        nativeAdLoader = createNativeAdLoader()
        nativeAdLoader?.loadAd(
            NativeAdRequestConfiguration.Builder(ID)
                .setContextTags(
                    listOf(
                        "доходы",
                        "деньги",
                        "инвестиции",
                        "автомобили"
                    )
                )
                .setContextQuery("уменьшение расходов на машину и увеличение доходов")
                .build()
        )
    }

    companion object Companion {
        private const val ID = "R-M-17186581-4"
    }
}