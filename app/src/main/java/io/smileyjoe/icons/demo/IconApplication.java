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

        ArrayList<String> preloadNames = new ArrayList<>();

        preloadNames.add("emoticon-wink");
        preloadNames.add("emoticon-tongue-outline");
        preloadNames.add("engine-outline");
        preloadNames.add("eraser");

        Icon.setup(getApplicationContext(), preloadNames, new SetupListener() {
            @Override
            public void setupComplete() {
                Log.d("IconThings", "SetupComplete");
            }

            @Override
            public void setupFailed() {
                Log.e("IconThings", "SetupFailed");
            }
        });
    }
}
