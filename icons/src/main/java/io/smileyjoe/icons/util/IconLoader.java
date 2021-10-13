package io.smileyjoe.icons.util;

import android.content.Context;
import android.text.TextUtils;

import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import io.smileyjoe.icons.IconData;
import io.smileyjoe.icons.db.Database;
import io.smileyjoe.icons.listener.IconLoaded;

public class IconLoader implements Runnable {

    public enum Key {
        ID, NAME
    }

    private String mValue;
    private Executor mMainExecutor;
    private IconLoaded mListener;
    private Context mContext;
    private Key mKey;

    private IconLoader(Context context) {
        mContext = context;
    }

    public static IconLoader with(Context context) {
        return new IconLoader(context);
    }

    public IconLoader key(Key key) {
        mKey = key;
        return this;
    }

    public IconLoader value(String value) {
        mValue = value;
        return this;
    }

    public IconLoader listener(IconLoaded listener) {
        mListener = listener;
        return this;
    }

    public void execute() {
        mMainExecutor = ContextCompat.getMainExecutor(mContext);
        ScheduledExecutorService backgroundExecutor = Executors.newSingleThreadScheduledExecutor();
        backgroundExecutor.execute(this);
    }

    @Override
    public void run() {
        IconData data = null;

        switch (mKey) {
            case ID:
                data = Database.getIconData().findById(mValue);
                break;
            case NAME:
                data = Database.getIconData().findByName(mValue);
                break;
        }

        if (data != null && !TextUtils.isEmpty(data.getPath())) {
            mMainExecutor.execute(new ReturnToUi(data));
        } else if (data != null && !TextUtils.isEmpty(data.getId())) {
            Api.getIcon(mContext, data.getId(), icon -> mMainExecutor.execute(new ReturnToUi(icon)));
        } else {
            // TODO: Something //
        }
    }

    private class ReturnToUi implements Runnable {
        private IconData mIconData;

        public ReturnToUi(IconData iconData) {
            mIconData = iconData;
        }

        @Override
        public void run() {
            mListener.onIconLoaded(mIconData);
        }
    }

}
