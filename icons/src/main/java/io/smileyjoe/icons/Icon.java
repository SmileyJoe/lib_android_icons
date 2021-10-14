package io.smileyjoe.icons;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.core.graphics.drawable.DrawableCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.smileyjoe.icons.db.Database;
import io.smileyjoe.icons.listener.IconLoaded;
import io.smileyjoe.icons.listener.SetupListener;
import io.smileyjoe.icons.util.IconCache;
import io.smileyjoe.icons.util.IconLoader;
import io.smileyjoe.icons.util.VectorDrawableCreator;

public class Icon {

    public static void setup(Context applicationContext, ArrayList<String> preloadNames, SetupListener listener) {
        Database.setup(applicationContext, preloadNames, listener);
    }

    public static void load(Context context, String name, IconLoaded listener) {
        IconLoader.with(context).name(name).listener(listener).load();
    }

    public static void load(Context context, String... names){
        load(context, new ArrayList<>(Arrays.asList(names)));
    }

    public static void load(Context context, ArrayList<String> names){
        IconLoader.with(context).names(names).load();
    }

    public static Drawable tint(Drawable drawable, int color, boolean clone){
        if(clone){
            return tint(drawable.getConstantState().newDrawable(), color);
        } else {
            return tint(drawable, color);
        }
    }

    public static Drawable tint(Drawable drawable, int color){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            drawable.setTint(color);
        } else {
            DrawableCompat.setTint(drawable, color);
        }

        return drawable;
    }

    public static Drawable fromPath(Context context, IconData icon) {
        return fromPath(context, icon, Color.BLACK);
    }

    public static Drawable fromPath(Context context, IconData icon, int color) {
        List<VectorDrawableCreator.PathData> pathList = Arrays.asList(
                new VectorDrawableCreator.PathData(icon.getPath(), color));

        Drawable drawable = VectorDrawableCreator.getVectorDrawable(
                context,
                24, 24, 24, 24,
                pathList);

        IconCache.getInstance().cache(icon.getName(), drawable);

        return drawable;
    }
}
