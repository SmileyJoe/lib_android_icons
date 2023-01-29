package io.smileyjoe.icons.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import io.smileyjoe.icons.extensions.DrawableExt.tint
import io.smileyjoe.icons.util.IconViewHelper

/**
 * TextView that takes custom attributes to load an icon into the left drawable
 * <br/>
 * Attributes:
 * - icon_name, name of the icon to load
 * - icon_color, color resource id
 * - icon_placeholder, drawable resource id for an animated vector, this isn't tinted by the icon_color
 * - icon_missing, drawable resource id for an image to show if the icon isn't found
 */
class IconTextView : AppCompatTextView, IconView {

    override var helper: IconViewHelper = IconViewHelper(this)

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs, defStyleAttr)
    }

    override fun applyTint() {
        if (helper.isLoaded) {
            compoundDrawables[0]?.let { drawable ->
                drawable.tint(helper.color)
            }
        }
    }

    override fun setDrawable(drawable: Drawable?) {
        setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
    }
}