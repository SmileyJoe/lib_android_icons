package io.smileyjoe.icons.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import io.smileyjoe.icons.Icon;
import io.smileyjoe.icons.listener.IconViewListener;
import io.smileyjoe.icons.util.IconViewHelper;

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

    @Override
    public void onIconLoaded(Drawable icon) {
        setCompoundDrawablesWithIntrinsicBounds(Icon.tint(icon, mHelper.getColor(), true), null, null, null);
    }

    @Override
    public void showPlaceholder(AnimatedVectorDrawableCompat placeholder) {
        setCompoundDrawablesWithIntrinsicBounds(placeholder, null, null, null);
    }
}
