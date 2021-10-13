package io.smileyjoe.icons;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.smileyjoe.icons.db.Database;
import io.smileyjoe.icons.listener.DrawableLoaded;
import io.smileyjoe.icons.listener.IconLoaded;
import io.smileyjoe.icons.listener.SetupListener;
import io.smileyjoe.icons.util.IconLoader;
import io.smileyjoe.icons.util.VectorDrawableCreator;

public class Icon {

    public static void setup(Context applicationContext, ArrayList<String> preloadNames, SetupListener listener) {
        Database.setup(applicationContext, preloadNames, listener);
    }

    public static void load(Context context, String name, IconLoaded listener) {
        IconLoader.with(context).value(name).listener(listener).execute();
    }

    public static void load(Context context, String name, DrawableLoaded listener){
        load(context, name, (icon) -> {
            listener.setIcon(context, icon);
        });
    }

    public static Drawable fromPath(Context context, String path, int color) {
        List<VectorDrawableCreator.PathData> pathList = Arrays.asList(
                new VectorDrawableCreator.PathData(path, color));

        Drawable drawable = VectorDrawableCreator.getVectorDrawable(
                context,
                24, 24, 24, 24,
                pathList);

        return drawable;
    }
}
