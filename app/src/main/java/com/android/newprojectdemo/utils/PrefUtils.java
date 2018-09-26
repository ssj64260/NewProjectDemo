package com.android.newprojectdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;

import com.android.newprojectdemo.app.APP;
import com.android.newprojectdemo.config.Constants;

import java.util.Set;

/**
 * sharedPreferences工具类
 */

public class PrefUtils {

    private static SharedPreferences getDefaultSp() {
        return PreferenceManager.getDefaultSharedPreferences(APP.getInstance());
    }

    private static SharedPreferences getUserInfoSp() {
        return APP.getInstance().getSharedPreferences(Constants.FILE_USER_INFO, Context.MODE_PRIVATE);
    }

    public static void setInUserInfo(@NonNull String key, @NonNull Object value) {
        SharedPreferences.Editor edit = getUserInfoSp().edit();
        set(edit, key, value);
    }

    public static void setInDefault(@NonNull String key, @NonNull Object value) {
        SharedPreferences.Editor edit = getDefaultSp().edit();
        set(edit, key, value);
    }

    private static void set(SharedPreferences.Editor edit, @NonNull String key, @NonNull Object value) {
        if (value instanceof String) {
            edit.putString(key, (String) value);
        } else if (value instanceof Integer) {
            edit.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            edit.putLong(key, (Long) value);
        } else if (value instanceof Boolean) {
            edit.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            edit.putFloat(key, (Float) value);
        } else if (value instanceof Set) {
            edit.putStringSet(key, (Set<String>) value);
        } else {
            throw new IllegalArgumentException(String.format("Type of value unsupported key=%s, value=%s", key, value));
        }
        edit.apply();
    }

    public static void clearAllUserInfo() {
        getUserInfoSp().edit().clear().apply();
    }

    public static void clearKey(@NonNull String fileName, @NonNull String key) {
        SharedPreferences sp = APP.getInstance().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sp.edit().remove(key).apply();
    }

    public static void clearKey(@NonNull String key) {
        getDefaultSp().edit().remove(key).apply();
    }

    public static String getLoginPhone() {
        return getDefaultSp().getString(Constants.KEY_LOGIN_PHONE, "");
    }

    public static String getLoginPassword() {
        return getDefaultSp().getString(Constants.KEY_LOGIN_PASSWORD, "");
    }

    public static String getAccessToken() {
        return getUserInfoSp().getString(Constants.KEY_ACCESS_TOKEN, "");
    }

}
