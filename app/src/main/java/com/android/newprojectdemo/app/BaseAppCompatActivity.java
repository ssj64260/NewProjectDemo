package com.android.newprojectdemo.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

import com.android.newprojectdemo.ui.dialog.DefaultProgressDialog;
import com.orhanobut.logger.Logger;

/**
 * 基类
 */

public class BaseAppCompatActivity extends AppCompatActivity {

    private InputMethodManager manager;
    private DefaultProgressDialog mProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Logger.i(this.getPackageName());
        Logger.i(this.getLocalClassName());

        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideKeyboard();
        hideProgress();
    }

    protected void hideKeyboard() {
        if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected void showProgress(String message) {
        if (mProgress == null) {
            mProgress = new DefaultProgressDialog(this);
        }
        mProgress.setMessage(message);
        mProgress.showDialog();
    }

    protected void hideProgress() {
        if (mProgress != null) {
            mProgress.dismissDialog();
        }
    }
}
