package io.smileyjoe.icons.demo;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import io.smileyjoe.icons.Icon;
import io.smileyjoe.icons.demo.databinding.ActivityMainBinding;
import io.smileyjoe.icons.listener.DrawableLoaded;
import io.smileyjoe.icons.util.IconLoader;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        mBinding.iconTwo.setIcon("format-text-rotation-up");

        Icon.load(getBaseContext(), "freebsd", (icon) -> {
            mBinding.imageIcon.setImageDrawable(Icon.fromPath(getBaseContext(), icon.getPath(), Color.BLUE));
        });

        Icon.load(getBaseContext(), "forum", new DrawableLoaded(Color.GREEN) {
            @Override
            public void onDrawableLoaded(Drawable drawable) {
                mBinding.imageIconThree.setImageDrawable(drawable);
            }
        });
    }
}