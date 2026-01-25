//
//  ImageCache.kt
//  AppCore
//
//  Created by Huseyn Hasanov on 24.01.26
//

package com.bonjur.designSystem.components.cashedImage

import android.graphics.Bitmap
import android.util.LruCache

object ImageCache {
    private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    private val cacheSize = maxMemory / 8 // Use 1/8th of available memory
    
    val shared: LruCache<String, Bitmap> = object : LruCache<String, Bitmap>(cacheSize) {
        override fun sizeOf(key: String, bitmap: Bitmap): Int {
            return bitmap.byteCount / 1024
        }
    }
    
    fun get(url: String): Bitmap? = shared.get(url)
    
    fun put(url: String, bitmap: Bitmap) {
        shared.put(url, bitmap)
    }
}