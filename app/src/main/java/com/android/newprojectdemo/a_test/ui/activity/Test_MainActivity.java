package com.android.newprojectdemo.a_test.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.android.newprojectdemo.R;
import com.android.newprojectdemo.a_test.ui.fragment.Test_HomeFragment;
import com.android.newprojectdemo.a_test.ui.fragment.Test_MessageFragment;
import com.android.newprojectdemo.a_test.ui.fragment.Test_MineFragment;
import com.android.newprojectdemo.app.BaseActivity;
import com.android.newprojectdemo.model.TabEntity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试用：首页
 */

public class Test_MainActivity extends BaseActivity {

    private List<Fragment> mFragmentList;
    private Fragment mFragment;

    private CommonTabLayout mTabLayout;

    private final int[] mIconSelect = {
            R.drawable.ic_android_black,
            R.drawable.ic_android_black,
            R.drawable.ic_android_black
    };
    private final int[] mIconUnselect = {
            R.drawable.ic_adb_gray,
            R.drawable.ic_adb_gray,
            R.drawable.ic_adb_gray
    };
    private final String[] mTabName = {
            "首页", "消息", "我的"
    };
    private ArrayList<CustomTabEntity> mTabEntities;

    public static void show(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, Test_MainActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.test_activity_main;
    }

    @Override
    protected void initData() {
        super.initData();
        mTabEntities = new ArrayList<>();
        for (int i = 0; i < mTabName.length; i++) {
            mTabEntities.add(new TabEntity(mTabName[i], mIconSelect[i], mIconUnselect[i]));
        }

        mFragmentList = new ArrayList<>();
        mFragmentList.add(Test_HomeFragment.newInstance());
        mFragmentList.add(Test_MessageFragment.newInstance());
        mFragmentList.add(Test_MineFragment.newInstance());
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mTabLayout = (CommonTabLayout) findViewById(R.id.tablayout);
        mTabLayout.setTabData(mTabEntities);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                showFragment(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        showFragment(0);
    }

    private void showFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (mFragment != null) {
            transaction.hide(mFragment);
        }

        if (position < mFragmentList.size()) {
            mFragment = mFragmentList.get(position);

            if (!mFragment.isAdded()) {
                transaction.add(R.id.fl_fragment, mFragment);
            }
            transaction.show(mFragment);
        }

        transaction.commit();
    }
}
