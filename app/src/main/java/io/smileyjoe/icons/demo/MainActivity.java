package io.smileyjoe.icons.demo;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import io.smileyjoe.icons.Icon;
import io.smileyjoe.icons.demo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View root = mBinding.getRoot();
        setContentView(root);

        mBinding.buttonSecond.setOnClickListener((view) -> startActivity(SecondActivity.getIntent(getBaseContext())));
        mBinding.iconTwo.setTint("#00FF00");
        mBinding.iconTwo.setIcon("format-text-rotation-up");

        Icon.load(getBaseContext(), "freebsd", (icon) -> mBinding.imageIcon.setImageDrawable(Icon.tint(icon, Color.BLUE)));

        Icon.load(getBaseContext(), "fridge", "format-size", "golf", "ghost");

        mBinding.iconTextTwo.setText("Set in code");
        mBinding.iconTextTwo.setIcon("heart-broken");
        mBinding.iconTextTwo.setTint("#FF0000");
    }
}