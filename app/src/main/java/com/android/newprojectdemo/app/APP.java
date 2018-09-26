package com.android.newprojectdemo.app;

import android.app.Application;

import com.android.newprojectdemo.R;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * application
 */

public class APP extends Application {

    private static APP mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;

        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .tag(getString(R.string.app_name))
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
    }

    public static APP getInstance(){
        return mApp;
    }

}
