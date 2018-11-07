package com.android.newprojectdemo.a_test.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.android.newprojectdemo.R;
import com.android.newprojectdemo.a_test.ui.fragment.Test_HomeFragment;
import com.android.newprojectdemo.a_test.ui.fragment.Test_MessageFragment;
import com.android.newprojectdemo.a_test.ui.fragment.Test_MineFragment;
import com.android.newprojectdemo.app.BaseActivity;
import com.android.newprojectdemo.model.TabEntity;
import com.android.newprojectdemo.widget.tablayout.CommonTabLayout;
import com.android.newprojectdemo.widget.tablayout.listener.CustomTabEntity;
import com.android.newprojectdemo.widget.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * 测试用：首页
 */

public class Test_MainActivity extends BaseActivity {

    private Fragment mFragment;
    private Fragment[] mFragments;

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
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mTabLayout = findViewById(R.id.tablayout);
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

        initFragment(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("activity_will_destory", true);
        super.onSaveInstanceState(outState);
    }

    private void initFragment(Bundle savedInstanceState) {
        final int tabCount = mTabEntities.size();

        mFragments = new Fragment[tabCount];
        if (savedInstanceState != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            List<Fragment> list = getSupportFragmentManager().getFragments();
            if (list != null && list.size() >= tabCount) {
                for (Fragment fragment : list) {
                    if (fragment instanceof Test_HomeFragment) {
                        mFragments[0] = fragment;
                    } else if (fragment instanceof Test_MessageFragment) {
                        mFragments[1] = fragment;
                    } else if (fragment instanceof Test_MineFragment) {
                        mFragments[2] = fragment;
                    }
                }
            }
            transaction.commit();
        }

        if (mFragments[0] == null) {
            mFragments[0] = Test_HomeFragment.newInstance();
        }
        if (mFragments[1] == null) {
            mFragments[1] = Test_MessageFragment.newInstance();
        }
        if (mFragments[2] == null) {
            mFragments[2] = Test_MineFragment.newInstance();
        }

        showFragment(0);
    }

    private void showFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (mFragment != null) {
            transaction.hide(mFragment);
        }

        if (position < mFragments.length) {
            mFragment = mFragments[position];

            if (!mFragment.isAdded()) {
                transaction.add(R.id.fl_fragment, mFragment);
            }
            transaction.show(mFragment);
        }

        transaction.commit();
    }
}
