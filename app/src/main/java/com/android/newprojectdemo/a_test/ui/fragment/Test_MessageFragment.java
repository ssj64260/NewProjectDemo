package com.android.newprojectdemo.a_test.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.newprojectdemo.R;
import com.android.newprojectdemo.app.BaseFragment;


/**
 * 消息
 */

public class Test_MessageFragment extends BaseFragment {

    public Test_MessageFragment() {

    }

    public static Test_MessageFragment newInstance() {
        return new Test_MessageFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        initToolbar();
    }

    private void initToolbar() {
        final ImageView ivBack = (ImageView) mFragmentView.findViewById(R.id.iv_toolbar_back);
        final TextView tvTitle = (TextView) mFragmentView.findViewById(R.id.tv_toolbar_title);

        ivBack.setVisibility(View.GONE);
        tvTitle.setText("消息");
    }
}
