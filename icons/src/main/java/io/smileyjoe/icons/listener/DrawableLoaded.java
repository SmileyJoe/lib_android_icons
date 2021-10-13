package io.smileyjoe.icons.listener;

import android.content.Context;
import android.graphics.drawable.Drawable;

import io.smileyjoe.icons.Icon;
import io.smileyjoe.icons.IconData;

public abstract class DrawableLoaded {

    public abstract void onDrawableLoaded(Drawable drawable);

    private int mColor;

    public DrawableLoaded(int color) {
        mColor = color;
    }

    public void setIcon(Context context, IconData icon){
        onDrawableLoaded(Icon.fromPath(context, icon.getPath(), mColor));
    }
}
