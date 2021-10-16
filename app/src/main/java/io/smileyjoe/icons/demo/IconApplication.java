package io.smileyjoe.icons.demo;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;

import io.smileyjoe.icons.Icon;
import io.smileyjoe.icons.listener.SetupListener;

public class IconApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Icon.setup(getApplicationContext());
    }
}
