package io.smileyjoe.icons.listener;

import android.graphics.drawable.Drawable;

import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

public interface IconViewListener extends IconLoaded {

    void showPlaceholder(AnimatedVectorDrawableCompat placeholder);
    void showMissing(Drawable drawable);
}
