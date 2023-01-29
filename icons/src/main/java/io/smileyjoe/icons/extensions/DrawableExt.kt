package io.smileyjoe.icons.extensions

import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat

object DrawableExt {
    /**
     * Tints a drawable with the option to clone it, see {@link #tint(Drawable, int)} for more info
     * <br/>
     * Because there is a {@link IconCache}, using the same icon with a different color
     * will change the color everywhere it is used. Passing true to the clone parameter
     * will clone the drawable before tinting it, so it doesn't change anywhere else.
     *
     * @param color color to tint it, already converted, not the resource id
     * @param clone cloning won't change the color of a cached drawable elsewhere it used
     * @return the tinted drawable
     */
    @JvmStatic
    fun Drawable.tint(@ColorInt color: Int, clone: Boolean): Drawable {
        if (clone) {
            if (constantState != null) {
                var drawable = constantState!!.newDrawable()
                drawable.tint(color)
                return drawable
            } else {
                return this
            }
        } else {
            tint(color)
            return this
        }
    }

    /**
     * Tints the icon, handles pre.post lollipop checks
     *
     * @param color color to tint it, already converted, not the resource id
     * @return tinted drawable
     */
    @JvmStatic
    fun Drawable.tint(@ColorInt color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTint(color)
        } else {
            DrawableCompat.setTint(this, color)
        }
    }
}