package io.smileyjoe.icons;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Icon {

    public static void setup(Context applicationContext, ArrayList<String> preloadNames){
        Database.setup(applicationContext, preloadNames);
    }

    public static void load(Context context, String name, IconLoader.Key key, IconLoaded listener){
        IconLoader.with(context).value(name).key(key).listener(listener).execute();
    }

    public static Drawable fromPath(Context context, String path, int color){
        List<VectorDrawableCreator.PathData> pathList = Arrays.asList(
                new VectorDrawableCreator.PathData(path, color));

        Drawable drawable = VectorDrawableCreator.getVectorDrawable(
                context,
                24, 24, 24, 24,
                pathList);

        return drawable;
    }
}
