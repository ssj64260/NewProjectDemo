package com.android.newprojectdemo.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.newprojectdemo.ui.dialog.DefaultProgressDialog;

/**
 * 基类
 */

public class BaseFragment extends Fragment {

    private DefaultProgressDialog mProgress;

    protected Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    protected void showProgress(String message) {
        if (mProgress == null) {
            mProgress = new DefaultProgressDialog(getActivity());
        }
        mProgress.setMessage(message);
        mProgress.showDialog();
    }

    protected void hideProgress() {
        if (mProgress != null) {
            mProgress.dismissDialog();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideProgress();
    }

    @Override
    public void onAttach(Context context) {
        Log.e(this.getClass().getSimpleName(), "onAttach");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e(this.getClass().getSimpleName(), "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(this.getClass().getSimpleName(), "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.e(this.getClass().getSimpleName(), "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.e(this.getClass().getSimpleName(), "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.e(this.getClass().getSimpleName(), "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.e(this.getClass().getSimpleName(), "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.e(this.getClass().getSimpleName(), "onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.e(this.getClass().getSimpleName(), "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.e(this.getClass().getSimpleName(), "onDetach");
        super.onDetach();
    }

    @Override
    public boolean getUserVisibleHint() {
        Log.e(this.getClass().getSimpleName(), "getUserVisibleHint");
        return super.getUserVisibleHint();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e(this.getClass().getSimpleName(), "setUserVisibleHint:" + isVisibleToUser);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e(this.getClass().getSimpleName(), "onHiddenChanged:" + hidden);
    }
}
