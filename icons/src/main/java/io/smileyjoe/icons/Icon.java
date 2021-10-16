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

/**
 * This is the main class to be used externally, it handles all the setup and loading of icons.
 * <br/>
 * Usage:
 * <br/>
 * {@code
 *      // setup in the application class
 *      Icon.setup(getApplicationContext(), preloadNames, new SetupListener());
 *
 *      // Preload icons into the db
 *      Icon.load(getBaseContext(), "fridge", "format-size", "golf", "ghost");
 *
 *      // Load an icon into a view
 *      Icon.load(getBaseContext(), "freebsd", (icon) -> imageView.setImageDrawable(Icon.tint(icon, Color.BLUE)));
 *
 *      // tint an icon
 *      Icon.tint(icon, Color.BLUE);
 *      // clone an icon and tint it
 *      Icon.tint(icon, Color.BLUE, true);
 * }
 */
public class Icon {

    /**
     * Convenience method for {@link #setup(Context, ArrayList, SetupListener) setup(applicationContext, null, null)}
     */
    public static void setup(Context applicationContext){
        setup(applicationContext, null, null);
    }

    /**
     * Sets up everything, this needs to be called in the application class.
     * 
     * @param applicationContext context
     * @param preloadNames icons to preload
     * @param listener callback
     */
    public static void setup(Context applicationContext, ArrayList<String> preloadNames, SetupListener listener) {
        Database.setup(applicationContext, preloadNames, listener);
    }

    /**
     * Load a single image
     * 
     * @param context context
     * @param name image to load
     * @param listener callback
     */
    public static void load(Context context, String name, IconLoaded listener) {
        IconLoader.with(context).name(name).listener(listener).load();
    }

    /**
     * Convenience method for {@link #load(Context, ArrayList) load(context, new ArrayList<>(Arrays.asList(names)));}
     */
    public static void load(Context context, String... names){
        load(context, new ArrayList<>(Arrays.asList(names)));
    }
    
    /**
     * Loads a list of images, this just gets the paths and saves them to the db
     *
     * @param context context
     * @param names icons to load
     */
    public static void load(Context context, ArrayList<String> names){
        IconLoader.with(context).names(names).load();
    }

    /**
     * Tints a drawable with the option to clone it, see {@link #tint(Drawable, int)} for more info
     * <br/>
     * Because there is a {@link IconCache}, using the same icon with a different color
     * will change the color everywhere it is used. Passing true to the clone parameter
     * will clone the drawable before tinting it, so it doesn't change anywhere else.
     *
     * @param drawable drawable to tint
     * @param color color to tint it, already converted, not the resource id
     * @param clone cloning won't change the color of a cached drawable elsewhere it used
     * @return the tinted drawable
     */
    public static Drawable tint(Drawable drawable, int color, boolean clone){
        if(clone){
            return tint(drawable.getConstantState().newDrawable(), color);
        } else {
            return tint(drawable, color);
        }
    }

    /**
     * Tints the icon, handles pre.post lollipop checks
     *
     * @param drawable drawable to tint
     * @param color color to tint it, already converted, not the resource id
     * @return tinted drawable
     */
    public static Drawable tint(Drawable drawable, int color){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            drawable.setTint(color);
        } else {
            DrawableCompat.setTint(drawable, color);
        }

        return drawable;
    }

    /**
     * Convenience method for {@link #fromPath(Context, IconData, int) fromPath(context, icon, Color.BLACK)}
     */
    public static Drawable fromPath(Context context, IconData icon) {
        return fromPath(context, icon, Color.BLACK);
    }

    /**
     * Converts the svg path into a drawable.
     * <br/>
     * Caches the drawable with {@link IconCache}
     *
     * @param context context
     * @param icon icon data with the path
     * @param color color for the tint
     * @return the drawable
     */
    public static Drawable fromPath(Context context, IconData icon, int color) {
        List<VectorDrawableCreator.PathData> pathList = Arrays.asList(
                new VectorDrawableCreator.PathData(icon.getPath(), color));

        // all the icons from materialdesign icons are 24,24,24,24, so we can hard code
        // it to that
        Drawable drawable = VectorDrawableCreator.getVectorDrawable(
                context,
                24, 24, 24, 24,
                pathList);

        IconCache.getInstance().cache(icon.getName(), drawable);

        return drawable;
    }
}
