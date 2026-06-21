package com.bonjur.app

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.bonjur.app.image.MinioHostInterceptor
import com.bonjur.network.AppConfig
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application(), ImageLoaderFactory {

    @Inject
    lateinit var appConfig: AppConfig

    override fun newImageLoader(): ImageLoader =
        ImageLoader.Builder(this)
            .components {
                add(MinioHostInterceptor(appConfig.apiBaseUrl))
            }
            .build()
}
