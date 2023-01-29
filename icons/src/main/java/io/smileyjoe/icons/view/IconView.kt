package io.smileyjoe.icons.view

import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import io.smileyjoe.icons.extensions.DrawableExt.tint
import io.smileyjoe.icons.listener.IconViewListener
import io.smileyjoe.icons.util.IconViewHelper

interface IconView : IconViewListener {

    var helper: IconViewHelper
    fun applyTint()
    fun setDrawable(drawable: Drawable?)

    fun init(attrs: AttributeSet? = null, defStyleAttr: Int = ResourcesCompat.ID_NULL) {
        helper.listener = this
        helper.load(attrs, defStyleAttr)
    }

    fun reset() {
        setDrawable(null)
        helper.reset()
    }

    fun setTint(@ColorRes color: Int) {
        helper.setColorRes(color)
        applyTint()
    }

    fun setTint(hex: String) {
        helper.setColorHex(hex)
        applyTint()
    }

    fun setIcon(name: String) {
        helper.load(name)
    }

    fun setMissing(@DrawableRes resId: Int) {
        helper.missing = resId
    }

    fun setPlaceholder(@DrawableRes resId: Int) {
        helper.placeholder = resId
    }

    override fun onIconLoaded(icon: Drawable?) {
        setDrawable(icon?.tint(helper.color, true))
    }

    override fun showPlaceholder(placeholder: AnimatedVectorDrawableCompat?) {
        setDrawable(placeholder)
    }

    override fun showMissing(drawable: Drawable?) {
        setDrawable(drawable?.tint(helper.color, true))
    }

}