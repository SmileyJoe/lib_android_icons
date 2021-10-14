package io.smileyjoe.icons.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import io.smileyjoe.icons.Icon;
import io.smileyjoe.icons.IconData;
import io.smileyjoe.icons.db.Database;
import io.smileyjoe.icons.listener.IconLoaded;

public class IconLoader implements Runnable {

    private Executor mMainExecutor;
    private IconLoaded mListener;
    private Context mContext;
    private ArrayList<String> mNames;

    private IconLoader(Context context) {
        mContext = context;
    }

    public static IconLoader with(Context context) {
        return new IconLoader(context);
    }

    public IconLoader name(String name) {
        if(mNames == null){
            mNames = new ArrayList<>();
        }
        mNames.add(name);
        return this;
    }

    public IconLoader names(ArrayList<String> names){
        mNames = names;
        return this;
    }

    public IconLoader listener(IconLoaded listener) {
        mListener = listener;
        return this;
    }

    public void load() {
        ArrayList<String> namesToLoad = new ArrayList<>();
        IconCache cache = IconCache.getInstance();

        for(String name:mNames){
            Drawable drawable = cache.get(name);

            if(drawable != null && mListener != null){
                mListener.onIconLoaded(drawable);
            } else {
                namesToLoad.add(name);
            }
        }

        if(!namesToLoad.isEmpty()) {
            mNames = namesToLoad;
            mMainExecutor = ContextCompat.getMainExecutor(mContext);
            Scheduler.getInstance().schedule(this);
        }
    }

    @Override
    public void run() {
        List<IconData> icons = Database.getIconData().findByNames(mNames);

        for(IconData data: icons) {
            if(mMainExecutor != null) {
                if (data != null && !TextUtils.isEmpty(data.getPath())) {
                    mMainExecutor.execute(new ReturnToUi(Icon.fromPath(mContext, data)));
                } else if (data != null && !TextUtils.isEmpty(data.getId())) {
                    Api.getIcon(mContext, data.getId(), icon -> mMainExecutor.execute(new ReturnToUi(icon)));
                } else {
                    // TODO: Something //
                }
            } else {
                Api.getIcon(mContext, data.getId(), null);
            }
        }
    }

    private class ReturnToUi implements Runnable {
        private Drawable mDrawable;

        public ReturnToUi(Drawable drawable) {
            mDrawable = drawable;
        }

        @Override
        public void run() {
            if(mListener != null) {
                mListener.onIconLoaded(mDrawable);
            }
        }
    }

}
