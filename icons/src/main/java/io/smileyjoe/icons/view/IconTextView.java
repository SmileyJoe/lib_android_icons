package io.smileyjoe.icons.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import io.smileyjoe.icons.Icon;
import io.smileyjoe.icons.IconData;
import io.smileyjoe.icons.listener.IconLoaded;
import io.smileyjoe.icons.util.IconViewHelper;

public class IconTextView extends AppCompatTextView implements IconLoaded {

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

    private void init(AttributeSet attrs, int defStyle){
        mHelper = new IconViewHelper(getContext());
        mHelper.setListener(this);
        mHelper.load(attrs, defStyle);
    }

    @Override
    public void onIconLoaded(IconData icon) {
        setCompoundDrawablesWithIntrinsicBounds(Icon.fromPath(getContext(), icon.getPath(), mHelper.getColor()), null, null, null);
    }
}
