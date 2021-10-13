package io.smileyjoe.icons.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import io.smileyjoe.icons.Icon;
import io.smileyjoe.icons.IconData;
import io.smileyjoe.icons.listener.IconLoaded;
import io.smileyjoe.icons.util.IconLoader;
import io.smileyjoe.icons.util.IconViewHelper;

public class IconImageView extends AppCompatImageView implements IconLoaded {

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
        mHelper = new IconViewHelper(getContext());
        mHelper.setListener(this);
        mHelper.load(attrs, defStyle);
    }

    public void setImageByName(String name) {
        mHelper.load(IconLoader.Key.NAME, name);
    }

    @Override
    public void onIconLoaded(IconData icon) {
        setImageDrawable(Icon.fromPath(getContext(), icon.getPath(), mHelper.getColor()));
    }
}
