package com.android.newprojectdemo.utils.alipay;

import android.text.TextUtils;

import java.util.Map;

import androidx.annotation.NonNull;

public class AliResultInfo {

    private static final String KEY_RESULT_STATUS = "resultStatus";
    private static final String KEY_MEMO = "memo";
    private static final String KEY_RESULT = "result";
    private static final String KEY_AUTH_CODE = "auth_code";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_RESULT_CODE = "result_code";

    private String resultStatus;
    private String result;
    private String memo;

    public AliResultInfo(Map<String, String> rawResult) {
        if (rawResult == null) {
            return;
        }

        for (String key : rawResult.keySet()) {
            if (TextUtils.equals(key, KEY_RESULT_STATUS)) {
                resultStatus = rawResult.get(key);
            } else if (TextUtils.equals(key, KEY_RESULT)) {
                result = rawResult.get(key);
            } else if (TextUtils.equals(key, KEY_MEMO)) {
                memo = rawResult.get(key);
            }
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "resultStatus={" + resultStatus + "};memo={" + memo
                + "};result={" + result + "}";
    }

    /**
     * @return the resultStatus
     */
    public String getResultStatus() {
        return resultStatus;
    }

    /**
     * @return the memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * @return the result
     */
    public String getResult() {
        return result;
    }

    public String getAuthCode() {
        return get(KEY_AUTH_CODE);
    }

    public String getUserId() {
        return get(KEY_USER_ID);
    }

    public String getResultCode() {
        return get(KEY_RESULT_CODE);
    }

    private String get(String type) {
        if (!TextUtils.isEmpty(result)) {
            final String[] keyValues = result.split("&");
            for (String keyValue : keyValues) {
                final String[] data = keyValue.split("=");
                if (data.length > 1 && type.equals(data[0])) {
                    return data[1];
                }
            }
        }
        return "";
    }
}