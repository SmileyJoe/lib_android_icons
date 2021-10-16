package io.smileyjoe.icons.util;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import io.smileyjoe.icons.Icon;
import io.smileyjoe.icons.R;
import io.smileyjoe.icons.listener.IconLoaded;
import io.smileyjoe.icons.listener.IconViewListener;

public class IconViewHelper implements IconLoaded {

    private int mColor;
    private IconViewListener mListener;
    private View mView;
    private int mIconMissingResId;

    public IconViewHelper(View view) {
        mView = view;
    }

    public int getColor() {
        return mColor;
    }

    public void setListener(IconViewListener listener) {
        mListener = listener;
    }

    public void load(AttributeSet attrs, int defStyle) {
        if (attrs != null) {
            TypedArray a = mView.getContext().obtainStyledAttributes(attrs, R.styleable.IconView, defStyle, 0);

            mColor = a.getColor(R.styleable.IconView_icon_color, Color.BLACK);

            handlePlaceholder(a.getResourceId(R.styleable.IconView_icon_placeholder, 0));
            mIconMissingResId = a.getResourceId(R.styleable.IconView_icon_missing, 0);
            load(a.getString(R.styleable.IconView_icon_name));

            a.recycle();
        }
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
        if(mListener != null && resId != 0) {
            IconCache cache = IconCache.getInstance();
            AnimatedVectorDrawableCompat animatedDrawable = (AnimatedVectorDrawableCompat) cache.get(resId);
            AnimatedVectorDrawableCompat placeholder;

            if(animatedDrawable != null) {
                placeholder = animatedDrawable;
                placeholder.clearAnimationCallbacks();
            } else {
                placeholder = AnimatedVectorDrawableCompat.create(mView.getContext(), resId);
                cache.cache(resId, placeholder);
            }

            placeholder.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                @Override
                public void onAnimationEnd(Drawable drawable) {
                    mView.post(() -> {
                        placeholder.start();
                    });
                }
            });
            placeholder.start();
            mListener.showPlaceholder(placeholder);
        }
    }

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
            } else {
                Drawable missing = getMissing();

                if(missing != null) {
                    mListener.showMissing(missing);
                }
            }
        }
    }
}
