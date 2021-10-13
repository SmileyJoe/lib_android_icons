package io.smileyjoe.icons.demo;

import android.app.Application;

import java.util.ArrayList;

import io.smileyjoe.icons.Icon;

public class IconApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ArrayList<String> preloadNames = new ArrayList<>();

        preloadNames.add("emoticon-wink");
        preloadNames.add("emoticon-tongue-outline");
        preloadNames.add("engine-outline");
        preloadNames.add("eraser");

        Icon.setup(getApplicationContext(), preloadNames);
    }
}
