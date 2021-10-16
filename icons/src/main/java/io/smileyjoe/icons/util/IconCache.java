package io.smileyjoe.icons.util;

import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.collection.LruCache;

/**
 * Memory cache for the icons
 * <br/>
 * Uses 2 LruCache caches, each using 1/25th of the available memory for this memory cache
 */
public class IconCache {

    private static IconCache sInstance;
    private LruCache<Integer, Drawable> mDrawables;
    private LruCache<String, Drawable> mIcons;

    private IconCache(){
        // https://developer.android.com/topic/performance/graphics/cache-bitmap //
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // Use 1/25th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 25;

        mDrawables = new LruCache<>(cacheSize);
        mIcons = new LruCache<>(cacheSize);
    }

    public static IconCache getInstance(){
        if (sInstance == null) {
            sInstance = new IconCache();
        }

        return sInstance;
    }

    /**
     * Add a drawable to the cache based on it's name
     *
     * @param name icon name
     * @param drawable icon
     */
    public void cache(String name, Drawable drawable){
        mIcons.put(name, drawable);
    }

    /**
     * Add a drawable to the cache based on it's resource id
     *
     * @param resId icon name
     * @param drawable icon
     */
    public void cache(@DrawableRes int resId, Drawable drawable){
        mDrawables.put(resId, drawable);
    }

    /**
     * Get a drawable from cache based on it's name
     *
     * @param name icon name
     * @return icon
     */
    public Drawable get(String name){
        return mIcons.get(name);
    }

    /**
     * Get a drawable from cache based on it's resource id
     *
     * @param resId icon resource id
     * @return icon
     */
    public Drawable get(@DrawableRes int resId){
        return mDrawables.get(resId);
    }

}
