package io.smileyjoe.icons.util;

import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.collection.LruCache;

public class IconCache {

    private static IconCache sInstance;
    private LruCache<Integer, Drawable> mDrawables;
    private LruCache<String, Drawable> mIcons;

    private IconCache(){
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

    public void cache(String name, Drawable drawable){
        mIcons.put(name, drawable);
    }

    public void cache(@DrawableRes int resId, Drawable drawable){
        mDrawables.put(resId, drawable);
    }

    public Drawable get(String name){
        return mIcons.get(name);
    }

    public Drawable get(@DrawableRes int resId){
        return mDrawables.get(resId);
    }

}
