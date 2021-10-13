package io.smileyjoe.icons.util;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import io.smileyjoe.icons.Icon;
import io.smileyjoe.icons.R;
import io.smileyjoe.icons.listener.IconViewListener;

public class IconViewHelper {

    private int mColor;
    private IconViewListener mListener;
    private View mView;

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
            String iconName = a.getString(R.styleable.IconView_icon_name);

            if (!TextUtils.isEmpty(iconName)) {
                load(iconName);
            }

            int placeholderResId = a.getResourceId(R.styleable.IconView_placeholder, 0);
            if(placeholderResId != 0){
                handlePlaceholder(placeholderResId);
            }

            a.recycle();
        }
    }

    private void handlePlaceholder(int resId){
        if(mListener != null) {
            AnimatedVectorDrawableCompat animatedDrawable = AnimatedVectorDrawableCompat.create(mView.getContext(), resId);
            animatedDrawable.setTint(mColor);
            mListener.showPlaceholder(animatedDrawable);
            animatedDrawable.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                @Override
                public void onAnimationEnd(Drawable drawable) {
                    mView.post(() -> {
                        animatedDrawable.start();
                    });
                }
            });
            animatedDrawable.start();
        }
    }

    public void load(String value) {
        if (!TextUtils.isEmpty(value)) {
            Icon.load(mView.getContext(), value, mListener);
        }
    }
}
