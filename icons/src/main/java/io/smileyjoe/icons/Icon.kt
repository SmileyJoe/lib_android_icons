package io.smileyjoe.icons

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import io.smileyjoe.icons.db.Database
import io.smileyjoe.icons.listener.IconLoaded
import io.smileyjoe.icons.listener.SetupListener
import io.smileyjoe.icons.util.IconCache
import io.smileyjoe.icons.util.IconLoader
import io.smileyjoe.icons.util.VectorDrawableCreator

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
 * }
 */
object Icon {

    /**
     * Convenience method for {@link #setup(Context, ArrayList, SetupListener) setup(applicationContext, null, null)}
     */
    @JvmStatic
    fun setup(applicationContext: Context) {
        setup(applicationContext, null, null)
    }

    /**
     * Sets up everything, this needs to be called in the application class.
     *
     * @param applicationContext context
     * @param preloadNames icons to preload
     * @param listener callback
     */
    @JvmStatic
    fun setup(applicationContext: Context, preloadNames: ArrayList<String>?, listener: SetupListener?) {
        Database.setup(applicationContext, preloadNames, listener)
    }

    /**
     * Load a single image
     *
     * @param context context
     * @param name image to load
     * @param listener callback
     */
    @JvmStatic
    fun load(context: Context, name: String, listener: IconLoaded?) {
        IconLoader.with(context).name(name).listener(listener).load()
    }

    /**
     * Convenience method for {@link #load(Context, ArrayList) load(context, new ArrayList<>(Arrays.asList(names)));}
     */
    @JvmStatic
    fun load(context: Context, vararg names: String) {
        load(context, arrayListOf(*names))
    }

    /**
     * Loads a list of images, this just gets the paths and saves them to the db
     *
     * @param context context
     * @param names icons to load
     */
    @JvmStatic
    fun load(context: Context, names: ArrayList<String>) {
        IconLoader.with(context).names(names).load()
    }

    /**
     * Convenience method for {@link #fromPath(Context, IconData, int) fromPath(context, icon, Color.BLACK)}
     */
    @JvmStatic
    fun fromPath(context: Context, icon: IconData): Drawable {
        return fromPath(context, icon, Color.BLACK)
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
    @JvmStatic
    fun fromPath(context: Context, icon: IconData, @ColorInt color: Int): Drawable {
        var pathList = listOf(VectorDrawableCreator.PathData(icon.path, color))

        // all the icons from pictogrammers icons are 24,24,24,24, so we can hard code
        // it to that
        var drawable = VectorDrawableCreator.getVectorDrawable(
                context,
                24, 24, 24F, 24F,
                pathList
        )

        IconCache.cache(icon.name, drawable)

        return drawable
    }

}