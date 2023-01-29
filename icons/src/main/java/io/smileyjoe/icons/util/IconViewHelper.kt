package io.smileyjoe.icons.util

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import io.smileyjoe.icons.Icon
import io.smileyjoe.icons.R
import io.smileyjoe.icons.listener.IconLoaded
import io.smileyjoe.icons.listener.IconViewListener

class IconViewHelper(val view: View) : IconLoaded {

    var listener: IconViewListener? = null
    var isLoaded: Boolean = false

    /**
     * Sets the color to tint the icons
     * <br/>
     *
     * This is the already converted color, see setColorHex and setColorRes
     * for other options
     *
     * @see setColorHex
     * @see setColorRes
     * @param color converted color for icon tint
     */
    @ColorInt
    var color: Int = 0

    @DrawableRes
    var placeholder: Int = ResourcesCompat.ID_NULL
        set(value) {
            field = value
            showPlaceholder()
        }

    @DrawableRes
    var missing: Int = 0

    fun setColorRes(@ColorRes resId: Int) {
        color = ContextCompat.getColor(view.context, resId)
    }

    fun setColorHex(hex: String) {
        color = Color.parseColor(hex)
    }

    private fun showMissing() {
        if (missing != ResourcesCompat.ID_NULL) {
            var icon: Drawable? = null

            IconCache.get(missing)?.let {
                icon = it
            } ?: run {
                icon = ContextCompat.getDrawable(view.context, missing)
                IconCache.cache(missing, icon)
            }

            listener?.showMissing(icon)
        }
    }

    private fun showPlaceholder() {
        if (listener != null && placeholder != ResourcesCompat.ID_NULL) {
            var icon = IconCache.get(placeholder) as AnimatedVectorDrawableCompat?

            if (icon != null) {
                icon.clearAnimationCallbacks()
            } else {
                icon = AnimatedVectorDrawableCompat.create(view.context, placeholder)
                IconCache.cache(placeholder, icon)
            }

            if (icon != null) {
                icon.apply {
                    registerAnimationCallback(PlaceholderCallback(icon))
                }.start()
                listener?.showPlaceholder(icon)
            }
        }
    }

    /**
     * Load the icon, this can be used to set the icon in code and not with the attributes,
     * see IconImageView.setIcon for an example
     *
     * @see io.smileyjoe.icons.view.IconImageView.setIcon
     * @param value icon name
     */
    fun load(name: String?) {
        if (!name.isNullOrEmpty()) {
            Icon.load(view.context, name, this)
        }
    }

    /**
     * Load details from the attributes set in the xml
     *
     * @param attrs attributes
     * @param defStyle
     */
    fun load(attrs: AttributeSet? = null, @AttrRes defStyle: Int = 0) {
        if (attrs != null) {
            view.context.obtainStyledAttributes(attrs, R.styleable.IconView, defStyle, 0)?.let { typedArray ->
                color = typedArray.getColor(R.styleable.IconView_icon_color, Color.BLACK)
                placeholder = typedArray.getResourceId(R.styleable.IconView_icon_placeholder, ResourcesCompat.ID_NULL)
                missing = typedArray.getResourceId(R.styleable.IconView_icon_missing, ResourcesCompat.ID_NULL)
                load(typedArray.getString(R.styleable.IconView_icon_name))
                typedArray.recycle()
            }
        }
    }

    /**
     * Reset the view, load the placeholder again if applicable
     */
    fun reset() {
        showPlaceholder()
    }

    override fun onIconLoaded(icon: Drawable?) {
        listener?.let { listener ->
            if (icon != null) {
                listener.onIconLoaded(icon)
                isLoaded = true
            } else {
                showMissing()
            }
        }
        if (listener != null) {
            if (icon != null) {
                listener?.onIconLoaded(icon)
            }
        }
    }

    /**
     * Used to loop the placeholder image, when the animation ends, start it up again
     */
    private inner class PlaceholderCallback(var icon: AnimatedVectorDrawableCompat) : Animatable2Compat.AnimationCallback() {
        override fun onAnimationEnd(drawable: Drawable?) {
            view.post { icon.start() }
        }
    }
}