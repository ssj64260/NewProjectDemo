package com.android.newprojectdemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.android.newprojectdemo.a_test.ui.activity.Test_RetrofitAndRxjavaActivity;
import com.android.newprojectdemo.app.BaseActivity;
import com.android.newprojectdemo.utils.PermissionsHelper;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;


/**
 * 启动页面
 */

public class SplashActivity extends BaseActivity {

    private PermissionsHelper mPermissionsHelper;

    @Override
    protected int getContentView() {
        return 0;
    }

    @Override
    protected void initData() {
        super.initData();

        mPermissionsHelper = new PermissionsHelper.Builder()
                .readCalendar()
                .writeCalendar()
                .camera()
                .readContacts()
                .writeContacts()
                .getAccounts()
                .accessFineLocation()
                .accessCoarseLocation()
                .recordAudio()
                .readPhoneState()
                .callPhone()
                .readCallLog()
                .writeCallLog()
                .useSip()
                .processOutgoingCalls()
                .sendSms()
                .receiveSms()
                .readSms()
                .receiveWapPush()
                .receiveMms()
                .readExternalStorage()
                .writeExternalStorage()
                .bodySensors()
                .setPermissionsResult(mPermissionsResult)
                .bulid();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        mPermissionsHelper.requestPermissions(this);
    }

    private void toMain() {
        Observable
                .timer(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(new Action() {
                    @Override
                    public void run() {
                        Test_RetrofitAndRxjavaActivity.show(SplashActivity.this);
                        finish();
                    }
                })
                .subscribe();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionsHelper.requestPermissionsResult(this, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPermissionsHelper.activityResult(this, requestCode);
    }

    @Override
    public void onBackPressed() {

    }

    private PermissionsHelper.OnPermissionsResult mPermissionsResult = new PermissionsHelper.OnPermissionsResult() {
        @Override
        public void allPermissionGranted() {
            toMain();
        }

        @Override
        public void cancelToSettings() {
            finish();
        }
    };

}
