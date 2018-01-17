package com.android.newprojectdemo.a_test.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.newprojectdemo.R;
import com.android.newprojectdemo.app.BaseFragment;


/**
 * 我的
 */

public class Test_MineFragment extends BaseFragment {

    public Test_MineFragment() {

    }

    public static Test_MineFragment newInstance() {
        return new Test_MineFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
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
        tvTitle.setText("我的");
    }
}
