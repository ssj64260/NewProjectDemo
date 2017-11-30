package com.android.newprojectdemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.newprojectdemo.R;
import com.android.newprojectdemo.a_test.ui.Test_LoginActivity;
import com.android.newprojectdemo.app.BaseActivity;

public class MainActivity extends BaseActivity {

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();

        startActivity(new Intent(this, Test_LoginActivity.class));
        finish();

    }

    private void initData() {

    }

    private void initView() {

    }

}
