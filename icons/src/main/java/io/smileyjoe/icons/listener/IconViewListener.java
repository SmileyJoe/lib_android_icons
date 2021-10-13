package io.smileyjoe.icons.listener;

import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

public interface IconViewListener extends IconLoaded {

    void showPlaceholder(AnimatedVectorDrawableCompat placeholder);
}
