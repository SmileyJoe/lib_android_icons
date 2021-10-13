package io.smileyjoe.icons.demo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;

import io.smileyjoe.icons.demo.R;

public class IconViewHelper {

    private Context mContext;
    private int mColor;
    private IconLoaded mListener;

    public IconViewHelper(Context context) {
        mContext = context;
    }

    public int getColor() {
        return mColor;
    }

    public void setListener(IconLoaded listener) {
        mListener = listener;
    }

    public void load(AttributeSet attrs, int defStyle){
        if(attrs != null) {
            TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.IconView, defStyle, 0);

            mColor = a.getColor(R.styleable.IconView_icon_color, Color.BLACK);
            String iconName = a.getString(R.styleable.IconView_icon_name);

            if(!TextUtils.isEmpty(iconName)){
                load(IconLoader.Key.NAME, iconName);
            } else {
                String iconId = a.getString(R.styleable.IconView_icon_id);
                load(IconLoader.Key.ID, iconId);
            }

            a.recycle();
        }
    }

    public void load(IconLoader.Key key, String value){
        if(!TextUtils.isEmpty(value)) {
            Icon.load(mContext, value, key, mListener);
        }
    }
}
