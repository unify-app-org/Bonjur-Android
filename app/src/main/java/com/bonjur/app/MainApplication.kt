package com.bonjur.app

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.bonjur.app.image.MinioHostInterceptor
import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.network.AppConfig
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application(), ImageLoaderFactory {

    @Inject
    lateinit var appConfig: AppConfig

    override fun onCreate() {
        super.onCreate()
        // Restore the stored app language before any screen composes.
        LanguageManager.init(this)
    }

    override fun newImageLoader(): ImageLoader =
        ImageLoader.Builder(this)
            .components {
                add(MinioHostInterceptor(appConfig.apiBaseUrl))
            }
            .build()
}
