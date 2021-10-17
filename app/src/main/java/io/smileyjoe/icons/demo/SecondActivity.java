package io.smileyjoe.icons.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import io.smileyjoe.icons.demo.databinding.ActivityMainBinding;
import io.smileyjoe.icons.demo.databinding.ActivitySecondBinding;

public class SecondActivity extends AppCompatActivity {

    private ActivitySecondBinding mBinding;

    public static Intent getIntent(Context context){
        return new Intent(context, SecondActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivitySecondBinding.inflate(getLayoutInflater());
        View root = mBinding.getRoot();
        setContentView(root);

        mBinding.buttonReset.setOnClickListener((view) -> {
            mBinding.iconOne.reset();
            mBinding.textOne.reset();
        });
    }
}