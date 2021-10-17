package io.smileyjoe.icons.view;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import io.smileyjoe.icons.Icon;
import io.smileyjoe.icons.IconData;
import io.smileyjoe.icons.R;
import io.smileyjoe.icons.listener.IconLoaded;
import io.smileyjoe.icons.listener.IconViewListener;
import io.smileyjoe.icons.util.IconLoader;
import io.smileyjoe.icons.util.IconViewHelper;

/**
 * ImageView that takes custom attributes to load icons
 * <br/>
 * Attributes:
 * - icon_name, name of the icon to load
 * - icon_color, color resource id
 * - icon_placeholder, drawable resource id for an animated vector, this isn't tinted by the icon_color
 * - icon_missing, drawable resource id for an image to show if the icon isn't found
 */
public class IconImageView extends AppCompatImageView implements IconViewListener {

    private IconViewHelper mHelper;

    public IconImageView(Context context) {
        super(context);
        init(null, 0);
    }

    public IconImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public IconImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        mHelper = new IconViewHelper(this);
        mHelper.setListener(this);
        mHelper.load(attrs, defStyle);
    }

    public void setTint(@ColorRes int color){
        mHelper.setColorResId(color);
        applyTint();
    }

    public void setTint(String hex){
        mHelper.setColor(hex);
        applyTint();
    }

    public void setIcon(String name) {
        mHelper.load(name);
    }

    /**
     * Apply the tint if the drawable is already set
     */
    private void applyTint(){
        if(mHelper.isLoaded() && getDrawable() != null){
            Icon.tint(getDrawable(), mHelper.getColor());
        }
    }

    /**
     * Reset the icon, this is helpful for lists
     */
    public void reset(){
        setImageDrawable(null);
        mHelper.reset();
    }

    @Override
    public void onIconLoaded(Drawable icon) {
        setImageDrawable(Icon.tint(icon, mHelper.getColor(), true));
    }

    @Override
    public void showPlaceholder(AnimatedVectorDrawableCompat placeholder) {
        setImageDrawable(placeholder);
    }

    @Override
    public void showMissing(Drawable drawable) {
        setImageDrawable(Icon.tint(drawable, mHelper.getColor(), true));
    }
}
