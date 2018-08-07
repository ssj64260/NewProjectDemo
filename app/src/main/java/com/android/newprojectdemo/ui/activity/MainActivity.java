package com.android.newprojectdemo.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.newprojectdemo.R;
import com.android.newprojectdemo.a_test.ui.activity.Test_LoginActivity;
import com.android.newprojectdemo.app.BaseActivity;

public class MainActivity extends BaseActivity {

    public static void show(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        Test_LoginActivity.show(this);
        finish();
    }

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    };
}
