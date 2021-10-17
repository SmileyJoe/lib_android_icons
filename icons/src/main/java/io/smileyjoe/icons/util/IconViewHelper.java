package io.smileyjoe.icons.util;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import io.smileyjoe.icons.Icon;
import io.smileyjoe.icons.R;
import io.smileyjoe.icons.listener.IconLoaded;
import io.smileyjoe.icons.listener.IconViewListener;

/**
 * Helper for custom views that will load in an icon
 * <br/>
 * Takes in the custom attributes like name, colour, placeholder, etc and process
 * them with a callback. Also deals with the {@link IconCache} for missing and placeholder images
 * <br/>
 * See {@link io.smileyjoe.icons.view.IconImageView} and {@link io.smileyjoe.icons.view.IconTextView} for example use
 */
public class IconViewHelper implements IconLoaded {

    // icon color, not the resource id
    private int mColor;
    private IconViewListener mListener;
    // view the helper is being used in
    private View mView;
    private int mIconMissingResId;
    private boolean mIsLoaded;
    private int mPlaceholderResId;

    public IconViewHelper(View view) {
        mView = view;
        mIsLoaded = false;
    }

    /**
     * Gets the color for the icon, this is already converted from a resource id or whatever else
     *
     * @return converted color
     */
    public int getColor() {
        return mColor;
    }

    public void setListener(IconViewListener listener) {
        mListener = listener;
    }

    /**
     * Load details from the attributes set in the xml
     *
     * @param attrs attributes
     * @param defStyle
     */
    public void load(AttributeSet attrs, int defStyle) {
        if (attrs != null) {
            TypedArray a = mView.getContext().obtainStyledAttributes(attrs, R.styleable.IconView, defStyle, 0);

            setColor(a.getColor(R.styleable.IconView_icon_color, Color.BLACK));

            handlePlaceholder(a.getResourceId(R.styleable.IconView_icon_placeholder, 0));
            // we only need to use this if the icon is missing, so don't process it now
            mIconMissingResId = a.getResourceId(R.styleable.IconView_icon_missing, 0);
            load(a.getString(R.styleable.IconView_icon_name));

            a.recycle();
        }
    }

    /**
     * Reset the view, load the placeholder again if applicable
     */
    public void reset(){
        handlePlaceholder(mPlaceholderResId);
    }

    public void setColorResId(@ColorRes int color){
        setColor(ContextCompat.getColor(mView.getContext(), color));
    }

    public void setColor(String hex){
        setColor(Color.parseColor(hex));
    }

    /**
     * Sets the color to tint the icons
     * <br/>
     * This is the already converted color, see {@link #setColor(String)} and {@link #setColorResId(int)}
     * for other options
     *
     * @param color converted color for icon tint
     */
    public void setColor(int color){
        mColor = color;
    }

    public boolean isLoaded() {
        return mIsLoaded;
    }

    private Drawable getMissing(){
        if(mIconMissingResId != 0){
            IconCache cache = IconCache.getInstance();
            Drawable drawable = cache.get(mIconMissingResId);

            if(drawable == null){
                drawable = ContextCompat.getDrawable(mView.getContext(), mIconMissingResId);
                cache.cache(mIconMissingResId, drawable);
            }

            return drawable;
        } else {
            return null;
        }
    }

    private void handlePlaceholder(int resId){
        // if there is no listener, there is nothing to do with the placeholder, so do nothing
        if(mListener != null && resId != 0) {
            mPlaceholderResId = resId;
            IconCache cache = IconCache.getInstance();
            AnimatedVectorDrawableCompat placeholder = (AnimatedVectorDrawableCompat) cache.get(resId);

            if(placeholder != null) {
                // clear any animations that where on the cached drawable
                placeholder.clearAnimationCallbacks();
            } else {
                placeholder = AnimatedVectorDrawableCompat.create(mView.getContext(), resId);
                cache.cache(resId, placeholder);
            }

            // add a callback to restart the animation so it loops
            placeholder.registerAnimationCallback(new PlaceholderCallback(placeholder));
            // start the animation
            placeholder.start();
            mListener.showPlaceholder(placeholder);
        }
    }

    /**
     * Load the icon, this can be used to set the icon in code and not with the attributes,
     * see {@link io.smileyjoe.icons.view.IconImageView#setIcon(String)} for an example
     *
     * @param value icon name
     */
    public void load(String value) {
        if (!TextUtils.isEmpty(value)) {
            Icon.load(mView.getContext(), value, this);
        }
    }

    @Override
    public void onIconLoaded(Drawable icon) {
        if(mListener != null){
            if(icon != null){
                mListener.onIconLoaded(icon);
                mIsLoaded = true;
            } else {
                Drawable missing = getMissing();

                if(missing != null) {
                    mListener.showMissing(missing);
                }
            }
        }
    }

    /**
     * Used to loop the placeholder image, when the animation ends, start it up again
     */
    private class PlaceholderCallback extends Animatable2Compat.AnimationCallback {
        private AnimatedVectorDrawableCompat mPlaceholder;

        public PlaceholderCallback(AnimatedVectorDrawableCompat placeholder) {
            mPlaceholder = placeholder;
        }

        @Override
        public void onAnimationEnd(Drawable drawable) {
            // we need to post it as a runnable else it can sometimes case a chase exception and crash
            mView.post(() -> {
                mPlaceholder.start();
            });
        }
    }
}
