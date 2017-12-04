package com.android.newprojectdemo.app;

import android.app.Application;

import com.android.newprojectdemo.R;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;

/**
 * application
 */

public class APP extends Application {

    private static APP INStANCE;

    public APP() {
        INStANCE = this;
    }

    public static APP getInstance() {
        if (INStANCE == null) {
            synchronized (APP.class) {
                if (INStANCE == null) {
                    INStANCE = new APP();
                }
            }
        }
        return INStANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);//内存泄漏监听

//        CrashReport.initCrashReport(getApplicationContext(), "ccf9eafa11", false);//Bugly

        Logger.init(getString(R.string.app_name));

    }
}
