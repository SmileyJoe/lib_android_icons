package io.smileyjoe.icons.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import io.smileyjoe.icons.Icon;
import io.smileyjoe.icons.listener.IconViewListener;
import io.smileyjoe.icons.util.IconViewHelper;

/**
 * TextView that takes custom attributes to load an icon into the left drawable
 * <br/>
 * Attributes:
 * - icon_name, name of the icon to load
 * - icon_color, color resource id
 * - icon_placeholder, drawable resource id for an animated vector, this isn't tinted by the icon_color
 * - icon_missing, drawable resource id for an image to show if the icon isn't found
 */
public class IconTextView extends AppCompatTextView implements IconViewListener {

    private IconViewHelper mHelper;

    public IconTextView(@NonNull Context context) {
        super(context);
        init(null, 0);
    }

    public IconTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public IconTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
     * Apply the tint if the left drawable is already set
     */
    private void applyTint(){
        if(mHelper.isLoaded()) {
            Drawable[] drawables = getCompoundDrawables();

            if (drawables != null && drawables[0] != null) {
                Icon.tint(drawables[0], mHelper.getColor());
            }
        }
    }

    /**
     * Reset the icon, this is helpful for lists
     */
    public void reset(){
        setLeftDrawable(null);
        mHelper.reset();
    }

    @Override
    public void onIconLoaded(Drawable icon) {
        setLeftDrawable(Icon.tint(icon, mHelper.getColor(), true));
    }

    @Override
    public void showPlaceholder(AnimatedVectorDrawableCompat placeholder) {
        setLeftDrawable(placeholder);
    }

    @Override
    public void showMissing(Drawable drawable) {
        setLeftDrawable(Icon.tint(drawable, mHelper.getColor(), true));
    }

    private void setLeftDrawable(Drawable drawable){
        setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }
}
