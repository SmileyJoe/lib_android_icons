package io.smileyjoe.icons.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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

    public void execute() {
        if(mListener != null) {
            mMainExecutor = ContextCompat.getMainExecutor(mContext);
            ScheduledExecutorService backgroundExecutor = Executors.newSingleThreadScheduledExecutor();
            backgroundExecutor.execute(this);
        }
    }

    @Override
    public void run() {
        List<IconData> icons = Database.getIconData().findByNames(mNames);

        for(IconData data: icons) {
            if(mMainExecutor != null) {
                if (data != null && !TextUtils.isEmpty(data.getPath())) {
                    mMainExecutor.execute(new ReturnToUi(data));
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
        private IconData mIconData;

        public ReturnToUi(IconData iconData) {
            mIconData = iconData;
        }

        @Override
        public void run() {
            if(mListener != null) {
                mListener.onIconLoaded(mIconData);
            }
        }
    }

}
