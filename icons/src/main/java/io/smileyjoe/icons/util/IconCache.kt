package io.smileyjoe.icons.util

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.collection.LruCache
import androidx.core.content.res.ResourcesCompat

/**
 * Memory cache for the icons
 * <br/>
 * Uses 2 LruCache caches, each using 1/25th of the available memory for this memory cache
 */
object IconCache {

    private val drawables: LruCache<Int, Drawable>
    private val icons: LruCache<String, Drawable>

    init {
        // https://developer.android.com/topic/performance/graphics/cache-bitmap //
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024)
        // Use 1/25th of the available memory for this memory cache. //
        var cacheSize = maxMemory / 25

        drawables = LruCache(cacheSize.toInt())
        icons = LruCache(cacheSize.toInt())
    }

    /**
     * Add a drawable to the cache based on it's name
     *
     * @param name icon name
     * @param drawable icon
     */
    fun cache(name: String?, drawable: Drawable?) {
        if (!name.isNullOrEmpty() && drawable != null) {
            icons.put(name, drawable)
        }
    }

    /**
     * Add a drawable to the cache based on it's resource id
     *
     * @param resId icon name
     * @param drawable icon
     */
    fun cache(@DrawableRes resId: Int, drawable: Drawable?) {
        if (resId != ResourcesCompat.ID_NULL && drawable != null) {
            drawables.put(resId, drawable)
        }
    }

    /**
     * Get a drawable from cache based on it's name
     *
     * @param name icon name
     * @return icon
     */
    fun get(name: String): Drawable? {
        if (!name.isNullOrEmpty()) {
            return icons.get(name)
        } else {
            return null
        }
    }

    /**
     * Get a drawable from cache based on it's resource id
     *
     * @param resId icon resource id
     * @return icon
     */
    fun get(@DrawableRes resId: Int): Drawable? {
        if (resId != ResourcesCompat.ID_NULL) {
            return drawables.get(resId)
        } else {
            return null
        }
    }
}