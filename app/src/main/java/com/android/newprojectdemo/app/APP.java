package com.android.newprojectdemo.app;

import android.app.Application;

import com.android.newprojectdemo.R;
import com.orhanobut.logger.Logger;

/**
 * application
 */

public class APP extends Application {

    private static APP mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;

        Logger.init(getString(R.string.app_name));
    }

    public static APP get(){
        return mApp;
    }

}
