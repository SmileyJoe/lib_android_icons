package io.smileyjoe.icons.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import io.smileyjoe.icons.Icon;
import io.smileyjoe.icons.IconData;
import io.smileyjoe.icons.db.Database;
import io.smileyjoe.icons.listener.IconLoaded;

/**
 * Loads icons in a background task, returns the icon in the ui task.
 * <br/>
 * - Checks if the icon path is in the cache, if it is, returns it
 * - Checks if the icon path is in the db, if it is, converts it to a drawable, caches it, and sends it back
 * - Gets the path from the api, stores it in the db, converts it to a drawable, caches it, and sends it back
 * <br/>
 * Usage:
 * {@code IconLoader.with(context).name(name).listener(listener).load();}
 */
public class IconLoader implements Runnable {

    // executor used to send the drawable back on the ui thread
    private Executor mMainExecutor;
    private IconLoaded mListener;
    private Context mContext;
    // icons to load
    private ArrayList<String> mNames;

    private IconLoader(Context context) {
        mContext = context;
    }

    public static IconLoader with(Context context) {
        return new IconLoader(context);
    }

    /**
     * Adds an icon name to the list to load, this means that this can be used
     * multiple times in the same load to batch load
     *
     * @param name icon to load
     * @return current instance
     */
    public IconLoader name(String name) {
        if (mNames == null) {
            mNames = new ArrayList<>();
        }
        mNames.add(name);
        return this;
    }

    /**
     * Load multiple icons, this will overide any names set in the same load
     *
     * @param names list of icon names
     * @return current instance
     */
    public IconLoader names(ArrayList<String> names) {
        mNames = names;
        return this;
    }

    public IconLoader listener(IconLoaded listener) {
        mListener = listener;
        return this;
    }

    /**
     * Load the icons, this will handle db updates, cacheing, etc
     */
    public void load() {
        ArrayList<String> namesToLoad = new ArrayList<>();
        IconCache cache = IconCache.getInstance();

        // go through the names, return any cached images immediately, add none cached icons to load
        for (String name : mNames) {
            Drawable drawable = cache.get(name);

            if (drawable != null && mListener != null) {
                mListener.onIconLoaded(drawable);
            } else {
                namesToLoad.add(name);
            }
        }

        // if there are any that aren't cached, add this loaded to the scheduler
        if (!namesToLoad.isEmpty()) {
            mNames = namesToLoad;
            // if there is no listener, the icons will just be loaded into the db, but nothing
            // is returned to the ui thread so we don't need to set this up
            if (mListener != null) {
                mMainExecutor = ContextCompat.getMainExecutor(mContext);
            }

            Scheduler.getInstance().schedule(this);
        }
    }

    @Override
    public void run() {
        List<IconData> icons = Database.getIconData().findByNames(mNames);

        if (icons != null && icons.size() > 0) {
            for (IconData data : icons) {
                // if this is not set, nothing needs to be returned
                if (mMainExecutor != null) {
                    if (data != null && !TextUtils.isEmpty(data.getPath())) {
                        // the icon is in the db and has a path, so send it back to the ui thread
                        mMainExecutor.execute(new ReturnToUi(Icon.fromPath(mContext, data)));
                    } else if (data != null && !TextUtils.isEmpty(data.getId())) {
                        // the icon is in the db, but has no path, so get it from the api, then send it back to the ui thread
                        Api.getIcon(mContext, data, icon -> mMainExecutor.execute(new ReturnToUi(icon)));
                    } else {
                        // something isn't right, just return null
                        mMainExecutor.execute(new ReturnToUi(null));
                    }
                } else if (data != null && TextUtils.isEmpty(data.getPath())) {
                    // if nothing is being returned, the only thing to do is load in any images
                    // that are requested but have no path.
                    // We don't need to check for errors or validity
                    Api.getIcon(mContext, data, null);
                }
            }
        } else {
            // the icon names aren't in the db, so we have no way to get them
            if(mMainExecutor != null) {
                mMainExecutor.execute(new ReturnToUi(null));
            }
        }
    }

    /**
     * Runnable that is run on {@link ContextCompat#getMainExecutor(Context)}
     * to return the icon to the UI thread
     */
    private class ReturnToUi implements Runnable {
        private Drawable mDrawable;

        public ReturnToUi(Drawable drawable) {
            mDrawable = drawable;
        }

        @Override
        public void run() {
            if (mListener != null) {
                mListener.onIconLoaded(mDrawable);
            }
        }
    }

}
